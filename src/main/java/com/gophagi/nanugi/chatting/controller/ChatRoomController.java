package com.gophagi.nanugi.chatting.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gophagi.nanugi.chatting.dto.ChatRoom;
import com.gophagi.nanugi.chatting.repository.ChatRoomRepository;
import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.common.auth.CommonAuthentication;
import com.gophagi.nanugi.groupbuying.exception.InvalidGroupbuyingBoardInstanceException;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;
import com.gophagi.nanugi.groupbuying.vo.BoardIdAndTitleVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatRoomController {

	private final GroupbuyingBoardQueryService groupbuyingBoardQueryService;
	private final ChatRoomRepository chatRoomRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final CommonAuthentication authentication;

	// 채팅 리스트 화면
	@GetMapping("${chatting.room-url}")
	public String room(Model model) {
		return "/chat/room";
	}

	// 유저의 모든 채팅방 목록 반환
	@GetMapping("${chatting.rooms-url}")
	@ResponseBody
	public List<ChatRoom> rooms(@CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return chatRoomRepository.findRoomsByUserId(String.valueOf(userId));
	}

	// 채팅방 생성
	@PostMapping("${chatting.create-room-url}")
	@ResponseBody
	public ChatRoom createRoom(@RequestParam Long boardId, @CookieValue String token) {

		Long userId = JwtTokenProvider.getUserIdFromJwt(token);

		authentication.isPromoter(userId, boardId);

		BoardIdAndTitleVO vo = groupbuyingBoardQueryService.retrieveBoardIdAndTitleVO(boardId);

		return chatRoomRepository.createChatRoom(vo);
	}

	// 채팅방 입장 화면
	@GetMapping("${chatting.room-detail-url}/{roomId}")
	public String roomDetail(Model model, @PathVariable Long roomId, @CookieValue String token) {

		Long userId = JwtTokenProvider.getUserIdFromJwt(token);

		authentication.hasAuthority(userId, roomId);

		model.addAttribute("roomId", roomId);

		return "/chat/roomdetail";
	}

	// 특정 채팅방 조회
	@GetMapping("${chatting.room-info-url}/{roomId}")
	@ResponseBody
	public ChatRoom roomInfo(@PathVariable Long roomId, @CookieValue String token) {

		Long userId = JwtTokenProvider.getUserIdFromJwt(token);

		authentication.hasAuthority(userId, roomId);

		return chatRoomRepository.findRoomByRoomId(String.valueOf(roomId));
	}

	@PostMapping("${chatting.remove-room-url}/{roomId}")
	@ResponseBody
	public void removeRoom(@PathVariable String roomId, @RequestBody List<String> participantIds) {
		// 게시글 삭제 확인 -> 게시글 존재하면 채팅룸 삭제 X
		try {

			groupbuyingBoardQueryService.retrieve(Long.valueOf(roomId));

		} catch (InvalidGroupbuyingBoardInstanceException e) {

			chatRoomRepository.removeRoom(participantIds, roomId);

		}
	}
}