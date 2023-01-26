package com.gophagi.nanugi.chatting.dto;

import java.io.Serializable;

import com.gophagi.nanugi.groupbuying.dto.BoardIdAndTitleDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	private String roomId;
	private String name;
	private long userCount;

	public static ChatRoom create(BoardIdAndTitleDTO dto) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = String.valueOf(dto.getId());
		chatRoom.name = dto.getTitle();
		return chatRoom;
	}
}