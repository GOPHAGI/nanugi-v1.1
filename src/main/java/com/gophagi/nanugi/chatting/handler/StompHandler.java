package com.gophagi.nanugi.chatting.handler;

import java.security.Principal;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.gophagi.nanugi.chatting.constant.MessageType;
import com.gophagi.nanugi.chatting.dto.ChatMessageDTO;
import com.gophagi.nanugi.chatting.repository.ChatRoomRepository;
import com.gophagi.nanugi.chatting.service.ChatService;
import com.gophagi.nanugi.common.auth.CommonAuthentication;
import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.groupbuying.exception.NoAuthorityException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatService chatService;
	private final CommonAuthentication authentication;

	// websocket을 통해 들어온 요청이 처리 되기전 실행된다.
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String jwtToken = accessor.getFirstNativeHeader("token");

		if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결 요청

			log.info("CONNECT {}", jwtToken);

			// Header의 jwt token 검증
			jwtTokenProvider.validateToken(jwtToken);

		} else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독 요청

			tryParticipate(message, jwtToken);

		} else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독 취소 요청

			// 취소 요청(token) or 강퇴 요청(header)
			if (isKickOutRequest(accessor)) {
				tryKickout(accessor, message);
				return message;
			}

			tryCancel(accessor, message, jwtToken);

		}

		return message;
	}

	private void tryParticipate(Message<?> message, String jwtToken) {
		// header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
		String roomId = getRoomIdBySimpDestination(message);

		// 채팅방에 들어온 클라이언트 userId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
		String userId = getUserIdBySimpUser(message);

		authentication.hasAuthority(Long.parseLong(userId), Long.parseLong(roomId));

		chatRoomRepository.setUserEnterInfo(userId, roomId);

		// 채팅방의 인원수를 +1한다.
		chatRoomRepository.plusUserCount(roomId);

		// 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
		String name = JwtTokenProvider.getUserNameFromJwt(jwtToken);
		chatService.sendChatMessage(buildChatMessage(roomId, name, MessageType.ENTER));

		log.info("SUBSCRIBED {}, {}, {}", userId, name, roomId);
	}

	private void tryCancel(StompHeaderAccessor accessor, Message<?> message, String jwtToken) {

		String userId = getUserIdBySimpUser(message);
		String name = JwtTokenProvider.getUserNameFromJwt(jwtToken);

		// subscribe.unsubscribe(id) / header에서 id 가져와서 채팅방 roomId를 얻는다.
		String roomId = getRoomId(message);

		// 유저의 채팅방 구독 취소 가능 여부 확인한다.
		checkAuthority(userId, roomId);

		// 채팅방의 인원수를 -1한다.
		chatRoomRepository.minusUserCount(roomId);

		// 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
		chatService.sendChatMessage(buildChatMessage(roomId, name, MessageType.QUIT));

		// 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
		chatRoomRepository.removeUserEnterInfo(userId, roomId);
	}

	private void checkAuthority(String userId, String roomId) {
		try {

			authentication.hasAuthority(Long.parseLong(userId), Long.parseLong(roomId));

		} catch (NoAuthorityException e) {

			log.info("(더이상) 참여자 아님");

			if (!chatRoomRepository.checkSubscribeRoom(userId, roomId)) {
				throw e;
			}
		}
	}

	private void tryKickout(StompHeaderAccessor accessor, Message<?> message) {

		String kickOutUserId = accessor.getFirstNativeHeader("kickoutId");
		String name = accessor.getFirstNativeHeader("kickoutName");

		// subscribe.unsubscribe(id) / header에서 id 가져와서 채팅방 roomId를 얻는다.
		String roomId = getRoomId(message);

		// 요청자가 게시자인지 확인한다.
		String userId = getUserIdBySimpUser(message);
		authentication.isPromoter(Long.parseLong(userId), Long.valueOf(roomId));

		// 강퇴한 유저의 채팅방 구독 취소 가능 여부 확인한다.
		checkAuthority(kickOutUserId, roomId);

		// 채팅방의 인원수를 -1한다.
		chatRoomRepository.minusUserCount(roomId);

		// 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
		chatService.sendChatMessage(buildChatMessage(roomId, name, MessageType.QUIT));

		// 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
		chatRoomRepository.removeUserEnterInfo(kickOutUserId, roomId);

		log.info("DISCONNECTED {}, {}, {}", kickOutUserId, name, roomId);
	}

	private boolean isKickOutRequest(StompHeaderAccessor accessor) {
		return accessor.containsNativeHeader("kickoutId") && accessor.containsNativeHeader("kickoutName");
	}

	private String getRoomId(Message<?> message) {
		return String.valueOf(message.getHeaders().getId());
	}

	private ChatMessageDTO buildChatMessage(String roomId, String name, MessageType messageType) {
		return ChatMessageDTO.builder().type(messageType).roomId(roomId).sender(name).build();
	}

	private String getUserIdBySimpUser(Message<?> message) {
		return Optional.ofNullable((Principal)message.getHeaders().get("simpUser"))
			.map(Principal::getName)
			.orElse("UnknownUser");
	}

	private String getRoomIdBySimpDestination(Message<?> message) {
		return chatService.getRoomId(
			Optional.ofNullable((String)message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
	}
}