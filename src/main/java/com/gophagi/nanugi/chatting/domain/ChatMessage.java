package com.gophagi.nanugi.chatting.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gophagi.nanugi.chatting.constant.MessageType;
import com.gophagi.nanugi.chatting.dto.ChatMessageDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ChatMessage {
	@Builder
	public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount) {
		this.type = type;
		this.roomId = roomId;
		this.sender = sender;
		this.message = message;
		this.userCount = userCount;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Enumerated(EnumType.STRING)
	private MessageType type; // 메시지 타입
	private String roomId; // 방번호
	private String sender; // 메시지 보낸사람
	private String message; // 메시지
	private long userCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용

	public static ChatMessage toChatMessage(ChatMessageDTO chatMessageDTO) {
		return ChatMessage.builder()
			.type(chatMessageDTO.getType())
			.roomId(chatMessageDTO.getRoomId())
			.sender(chatMessageDTO.getSender())
			.message(chatMessageDTO.getMessage())
			.userCount(chatMessageDTO.getUserCount())
			.build();
	}
}