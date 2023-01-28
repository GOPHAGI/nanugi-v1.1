package com.gophagi.nanugi.chatting.dto;

import java.io.Serializable;

import com.gophagi.nanugi.groupbuying.vo.BoardIdAndTitleVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	private String roomId;
	private String name;
	private long userCount;

	public static ChatRoom create(BoardIdAndTitleVO vo) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = String.valueOf(vo.getId());
		chatRoom.name = vo.getTitle();
		return chatRoom;
	}
}