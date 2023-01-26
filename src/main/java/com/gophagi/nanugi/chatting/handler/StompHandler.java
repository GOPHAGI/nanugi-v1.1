package com.gophagi.nanugi.chatting.handler;

import java.security.Principal;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.gophagi.nanugi.chatting.dto.ChatMessage;
import com.gophagi.nanugi.chatting.repository.ChatRoomRepository;
import com.gophagi.nanugi.chatting.service.ChatService;
import com.gophagi.nanugi.common.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatService chatService;

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
		chatRoomRepository.setUserEnterInfo(userId, roomId);

		// 채팅방의 인원수를 +1한다.
		chatRoomRepository.plusUserCount(roomId);

		// 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
		String name = JwtTokenProvider.getUserNameFromJwt(jwtToken);
		chatService.sendChatMessage(buildChatMessage(roomId, name, ChatMessage.MessageType.ENTER));

		log.info("SUBSCRIBED {}, {}, {}", userId, name, roomId);
	}

	private void tryCancel(StompHeaderAccessor accessor, Message<?> message, String jwtToken) {

		String userId = getUserIdBySimpUser(message);
		String name = JwtTokenProvider.getUserNameFromJwt(jwtToken);

		// subscribe.unsubscribe(id) / header에서 id 가져와서 채팅방 roomId를 얻는다.
		String roomId = getRoomId(message);

		// 채팅방의 인원수를 -1한다.
		chatRoomRepository.minusUserCount(roomId);

		// 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
		chatService.sendChatMessage(buildChatMessage(roomId, name, ChatMessage.MessageType.QUIT));

		// 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
		chatRoomRepository.removeUserEnterInfo(userId, roomId);
	}

	private void tryKickout(StompHeaderAccessor accessor, Message<?> message) {

		String userId = accessor.getFirstNativeHeader("kickoutId");
		String name = accessor.getFirstNativeHeader("kickoutName");

		// subscribe.unsubscribe(id) / header에서 id 가져와서 채팅방 roomId를 얻는다.
		String roomId = getRoomId(message);

		// 채팅방의 인원수를 -1한다.
		chatRoomRepository.minusUserCount(roomId);

		// 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
		chatService.sendChatMessage(buildChatMessage(roomId, name, ChatMessage.MessageType.QUIT));

		// 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
		chatRoomRepository.removeUserEnterInfo(userId, roomId);

		log.info("DISCONNECTED {}, {}, {}", userId, name, roomId);
	}

	private boolean isKickOutRequest(StompHeaderAccessor accessor) {
		return accessor.containsNativeHeader("kickoutId") && accessor.containsNativeHeader("kickoutName");
	}

	private String getRoomId(Message<?> message) {
		return String.valueOf(message.getHeaders().getId());
	}

	private ChatMessage buildChatMessage(String roomId, String name, ChatMessage.MessageType messageType) {
		return ChatMessage.builder().type(messageType).roomId(roomId).sender(name).build();
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