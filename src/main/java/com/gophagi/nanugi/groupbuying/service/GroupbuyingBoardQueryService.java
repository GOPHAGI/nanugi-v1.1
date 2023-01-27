package com.gophagi.nanugi.groupbuying.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.dto.BoardIdAndTitleDTO;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingThumbnailDTO;
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
		return GroupbuyingBoardDTO.toGroupbuyingBoardDTO(getGroupbuyingBoard(boardId));
	}

	@Transactional
	public List<GroupbuyingBoardDTO> searchGroupbuyingBoardByUserId(Long userId) {

		List<ParticipantDTO> participants = participantService.retrieveByUserId(userId);

		List<GroupbuyingBoardDTO> groupbuyingBoards = new ArrayList<>();
		for (ParticipantDTO participantDTO : participants) {
			groupbuyingBoards.add(participantDTO.getGroupbuyingBoard());
		}

		return groupbuyingBoards;
	}

	@Transactional
	public List<GroupbuyingBoardDTO> searchGroupbuyingBoardByUserIdAndRole(Long userId, Role role) {

		List<ParticipantDTO> participants = participantService.retrieveByUserIdAndRole(userId, role);

		List<GroupbuyingBoardDTO> groupbuyingBoards = new ArrayList<>();
		for (ParticipantDTO dto : participants) {
			groupbuyingBoards.add(dto.getGroupbuyingBoard());
		}

		return groupbuyingBoards;
	}

	@Transactional
	public Page<GroupbuyingThumbnailDTO> retrieveList(int page) {

		PageRequest pageRequest = PageRequest.of(page, 20);
		Page<GroupbuyingBoard> groupbuyingBoards = repository.findAll(pageRequest);

		return new PageImpl<>(
			GroupbuyingThumbnailDTO.toGroupbuyingThumbnailDTOs(groupbuyingBoards.getContent()),
			pageRequest, groupbuyingBoards.getTotalElements());
	}

	@Transactional
	public Page<GroupbuyingThumbnailDTO> retrieveCategoryList(Category category, int page) {

		PageRequest pageRequest = PageRequest.of(page, 20);
		Page<GroupbuyingBoard> groupbuyingBoards = repository.findByCategory(category, pageRequest);

		return new PageImpl<>(
			GroupbuyingThumbnailDTO.toGroupbuyingThumbnailDTOs(groupbuyingBoards.getContent()),
			pageRequest, groupbuyingBoards.getTotalElements());
	}

	@Transactional
	public BoardIdAndTitleDTO retrieveBoardIdAndTitleDTO(Long boardId) {
		return BoardIdAndTitleDTO.toGroupbuyingBoardDTO(getGroupbuyingBoard(boardId));
	}

	private GroupbuyingBoard getGroupbuyingBoard(Long boardId) {
		return repository.findById(boardId)
			.orElseThrow(() -> new InvalidGroupbuyingBoardInstanceException(ErrorCode.INVALID_BOARD_INSTANCE));
	}
}
