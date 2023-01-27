package com.gophagi.nanugi.groupbuying.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.common.util.authentication.CommonAuthentication;
import com.gophagi.nanugi.common.util.file.domain.Photo;
import com.gophagi.nanugi.common.util.file.service.FileService;
import com.gophagi.nanugi.groupbuying.constant.Status;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.domain.Participant;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardUpdateDTO;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.DuplicateParticipationException;
import com.gophagi.nanugi.groupbuying.exception.InvalidGroupbuyingBoardInstanceException;
import com.gophagi.nanugi.groupbuying.exception.PersonnelLimitExceededException;
import com.gophagi.nanugi.groupbuying.repository.GroupbuyingBoardRepository;

@Service
public class GroupbuyingBoardCommandService {
	private final GroupbuyingBoardRepository repository;
	private final ParticipantService participantService;
	private final FileService fileService;
	private final CommonAuthentication authentication;

	public GroupbuyingBoardCommandService(GroupbuyingBoardRepository repository,
		ParticipantService participantService, FileService fileService, CommonAuthentication authentication) {
		this.repository = repository;
		this.participantService = participantService;
		this.fileService = fileService;
		this.authentication = authentication;
	}

	@Transactional
	public void create(GroupbuyingBoardDTO dto, Long userId) {
		try {
			GroupbuyingBoard groupbuyingBoard = GroupbuyingBoard.toGroupbuyingBoard(dto);

			repository.save(groupbuyingBoard);

			fileService.saveAllPhotos(Photo.findNewPhotos(dto, groupbuyingBoard));

			participantService.createAsPromoter(userId, groupbuyingBoard);

		} catch (Exception exception) {
			throw new InvalidGroupbuyingBoardInstanceException(ErrorCode.INSERT_ERROR);
		}
	}

	@Transactional
	public void update(GroupbuyingBoardUpdateDTO dto, Long userId) {

		authentication.hasAuthority(userId,dto.getId());

		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(dto.getId());
		groupbuyingBoard.deletePhoto(dto.getDeletePhotoIdList());
		groupbuyingBoard.update(dto);
		fileService.saveAllPhotos(Photo.findNewPhotos(dto,groupbuyingBoard));
	}

	@Transactional
	public void order(Long userId, Long boardId) {
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);

		if (groupbuyingBoard.participateInDuplicate(userId)) {
			throw new DuplicateParticipationException(ErrorCode.PARTICIPATION_DUPLICATION);
		}
		if (groupbuyingBoard.isFull()) {
			throw new PersonnelLimitExceededException(ErrorCode.PERSONNEL_LIMIT_EXCEEDED);
		}
		participantService.createAsParticipant(userId, groupbuyingBoard);
	}

	@Transactional
	public void cancel(Long userId, Long boardId) {
		ParticipantDTO dto = authentication.isParticipant(userId, boardId);

		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);

		if (groupbuyingBoard.getStatus() != Status.GATHERING) {
			throw new IllegalStateException(ErrorCode.CANNOT_CANCEL_REQUEST.getMessage());
		}

		participantService.delete(dto.getId());
	}

	@Transactional
	public List<Long> remove(Long userId, Long boardId) {

		authentication.isPromoter(userId, boardId);

		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);
		if (groupbuyingBoard.getStatus() == Status.DONE) {
			throw new IllegalStateException(ErrorCode.CANNOT_DELETE_BOARD.getMessage());
		}
		// 채팅방 삭제를 위해 참여자 id 리스트 반환
		List<Long> participantIds = groupbuyingBoard.getParticipants()
			.stream()
			.map(Participant::getId)
			.collect(Collectors.toList());

		if (groupbuyingBoard.getStatus() == Status.DONE) {
			throw new RuntimeException();
		}
		repository.delete(groupbuyingBoard);

		return participantIds;
	}

	@Transactional
	public void updateView(Long boardId) {
		repository.updateView(boardId);
	}

	private GroupbuyingBoard getGroupbuyingBoard(Long boardId) {
		return repository.findById(boardId)
			.orElseThrow(() -> new InvalidGroupbuyingBoardInstanceException(ErrorCode.RETRIEVE_ERROR));
	}

}