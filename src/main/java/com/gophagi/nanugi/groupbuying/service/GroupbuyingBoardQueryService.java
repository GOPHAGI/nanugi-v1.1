package com.gophagi.nanugi.groupbuying.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.InvalidGroupbuyingBoardInstanceException;
import com.gophagi.nanugi.groupbuying.repository.GroupbuyingBoardRepository;

@Service
public class GroupbuyingBoardQueryService {
	private final GroupbuyingBoardRepository repository;
	private final ParticipantService participantService;

	public GroupbuyingBoardQueryService(GroupbuyingBoardRepository repository, ParticipantService participantService) {
		this.repository = repository;
		this.participantService = participantService;
	}

	@Transactional
	public GroupbuyingBoardDTO retrieve(Long boardId) {
		GroupbuyingBoard groupbuyingBoard = repository.findById(boardId)
			.orElseThrow(() -> new InvalidGroupbuyingBoardInstanceException());
		return GroupbuyingBoardDTO.toGroupbuyingBoardDTO(groupbuyingBoard);
	}

	@Transactional
	public List<GroupbuyingBoardDTO> searchGroupbuyingBoardByUserId(Long userId) {
		List<ParticipantDTO> participants = participantService.retrieveByUserId(userId);

		List<GroupbuyingBoardDTO> groupbuyingBoards = new ArrayList<>();
		for (ParticipantDTO dto : participants) {
			groupbuyingBoards.add(GroupbuyingBoardDTO.toGroupbuyingBoardDTO(dto.getGroupbuyingBoard()));
		}
		return groupbuyingBoards;
	}

	@Transactional
	public List<GroupbuyingBoardDTO> searchGroupbuyingBoardByUserIdAndRole(Long userId, Role role) {
		List<ParticipantDTO> participants = participantService.retrieveByUserIdAndRole(userId, role);

		List<GroupbuyingBoardDTO> groupbuyingBoards = new ArrayList<>();
		for (ParticipantDTO dto : participants) {
			groupbuyingBoards.add(GroupbuyingBoardDTO.toGroupbuyingBoardDTO(dto.getGroupbuyingBoard()));
		}
		return groupbuyingBoards;
	}
}
