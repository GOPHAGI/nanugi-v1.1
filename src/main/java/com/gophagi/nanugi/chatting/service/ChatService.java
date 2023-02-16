package com.gophagi.nanugi.chatting.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.gophagi.nanugi.chatting.constant.MessageType;
import com.gophagi.nanugi.chatting.domain.ChatMessage;
import com.gophagi.nanugi.chatting.dto.ChatMessageDTO;
import com.gophagi.nanugi.chatting.repository.ChatMessageRepository;
import com.gophagi.nanugi.chatting.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

	private final ChannelTopic channelTopic;
	private final RedisTemplate redisTemplate;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;

	/**
	 * destination정보에서 roomId 추출
	 */
	public String getRoomId(String destination) {
		int lastIndex = destination.lastIndexOf('/');
		if (lastIndex != -1)
			return destination.substring(lastIndex + 1);
		else
			return "";
	}

	/**
	 * 채팅방에 메시지 발송
	 */
	public void sendChatMessage(ChatMessageDTO chatMessageDTO) {
		chatMessageDTO.setUserCount(chatRoomRepository.getUserCount(chatMessageDTO.getRoomId()));
		if (MessageType.ENTER.equals(chatMessageDTO.getType())) {
			chatMessageDTO.setMessage(chatMessageDTO.getSender() + "님이 방에 입장했습니다.");
			chatMessageDTO.setSender("[알림]");
		} else if (MessageType.QUIT.equals(chatMessageDTO.getType())) {
			chatMessageDTO.setMessage(chatMessageDTO.getSender() + "님이 방에서 나갔습니다.");
			chatMessageDTO.setSender("[알림]");
		}
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageDTO);
	}

	public List<ChatMessage> loadChatHistoryByRoomId(String roomId) {
		return chatMessageRepository.findByRoomId(roomId);
	}
}
