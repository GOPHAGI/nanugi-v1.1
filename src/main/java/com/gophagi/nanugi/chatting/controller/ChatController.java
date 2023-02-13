package com.gophagi.nanugi.chatting.controller;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.gophagi.nanugi.chatting.dto.ChatMessageDTO;
import com.gophagi.nanugi.chatting.repository.ChatRoomRepository;
import com.gophagi.nanugi.chatting.service.ChatService;
import com.gophagi.nanugi.common.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final JwtTokenProvider jwtTokenProvider;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatService chatService;

	/**
	 * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
	 */
	@MessageMapping("${chatting.message-url}")
	public void message(ChatMessageDTO message, @Header("token") String token) {
		String nickname = jwtTokenProvider.getUserNameFromJwt(token);
		// 로그인 회원 정보로 대화명 설정
		message.setSender(nickname);
		// 채팅방 인원수 세팅
		message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
		// Websocket에 발행된 메시지를 redis로 발행(publish)
		chatService.sendChatMessage(message);
	}
}
