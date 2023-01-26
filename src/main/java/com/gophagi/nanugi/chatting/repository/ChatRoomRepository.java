package com.gophagi.nanugi.chatting.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.gophagi.nanugi.chatting.dto.ChatRoom;
import com.gophagi.nanugi.groupbuying.dto.BoardIdAndTitleDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
	/**
	 * Redis CacheKeys
	 * */
	private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
	public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
	@Resource(name = "redisTemplate")
	private SetOperations<String, String> setOpsEnterInfo;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;

	/**
	 * 모든 채팅방 조회
	 * */
	public List<ChatRoom> findAllRoom() {
		return hashOpsChatRoom.values(CHAT_ROOMS);
	}

	/**
	 * 사용자가 참여중인 모든 채팅방 조회
	 * */
	public List<ChatRoom> findRoomsByUserId(String userId) {

		Map<String, ChatRoom> chatRoomMap = hashOpsChatRoom.entries(CHAT_ROOMS);

		Set<String> roomIds = setOpsEnterInfo.members(userId);
		List<ChatRoom> enteredRooms = new ArrayList<>();

		for (String roomId : roomIds) {
			enteredRooms.add(chatRoomMap.get(roomId));
		}

		return enteredRooms;
	}

	/**
	 * 특정 채팅방 조회
	 * */
	public ChatRoom findRoomByRoomId(String roomId) {
		return hashOpsChatRoom.get(CHAT_ROOMS, roomId);
	}

	/**
	 * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
	 * */
	public ChatRoom createChatRoom(BoardIdAndTitleDTO dto) {

		ChatRoom chatRoom = ChatRoom.create(dto);
		hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
		return chatRoom;
	}

	/**
	 * 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
	 * */
	public void setUserEnterInfo(String userId, String roomId) {
		setOpsEnterInfo.add(userId, roomId);
	}

	/**
	 * 유저 세션으로 입장해 있는 참여하고 있는 채팅방 ID 조회
	 * */
	public Set<String> getUserEnterRoomIds(String userId) {
		return setOpsEnterInfo.members(userId);
	}

	/**
	 * 유저 세션정보와 맵핑된 채팅방ID 삭제
	 * */
	public void removeUserEnterInfo(String userId, String roomId) {
		setOpsEnterInfo.remove(userId, roomId);
	}

	/**
	 * 채팅방 유저수 조회
	 * */
	public long getUserCount(String roomId) {
		return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
	}

	/**
	 * 채팅방에 입장한 유저수 +1
	 * */
	public long plusUserCount(String roomId) {
		return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
	}

	/**
	 * 채팅방에 입장한 유저수 -1
	 * */
	public long minusUserCount(String roomId) {
		return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
	}

	/**
	 * 채팅방 삭제
	 * 해당 채팅방 유저 mapping 정보 삭제
	 * 해당 채팅방 조회수 정보 삭제
	 * */
	public void removeRoom(List<String> participantIds, String roomId) {
		hashOpsChatRoom.delete(CHAT_ROOMS, roomId);
		for (String participantId : participantIds) {
			setOpsEnterInfo.remove(participantId, roomId);
		}
		valueOps.getAndDelete(USER_COUNT + "_" + roomId);
	}
}