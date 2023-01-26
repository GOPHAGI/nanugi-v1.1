package com.gophagi.nanugi.groupbuying.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gophagi.nanugi.common.util.authentication.CommonAuthentication;
import com.gophagi.nanugi.common.util.file.service.FileService;
import com.gophagi.nanugi.groupbuying.constant.Status;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.domain.Participant;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
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
	public void create(GroupbuyingBoardDTO dto, List<MultipartFile> files, Long userId) {
		if (Objects.isNull(dto)) {
			throw new NullPointerException("dto is empty");
		}
		try {
			GroupbuyingBoard groupbuyingBoard = GroupbuyingBoard.toGroupbuyingBoard(dto);

			repository.save(groupbuyingBoard);

			participantService.createAsPromoter(userId, groupbuyingBoard);

			fileService.saveFiles(userId, groupbuyingBoard, files);

		} catch (Exception exception) {
			throw new InvalidGroupbuyingBoardInstanceException();
		}
	}

	@Transactional
	public void update(GroupbuyingBoardDTO dto, List<MultipartFile> files, List<Long> deletePhotoIdList, Long userId) {
		if (Objects.isNull(dto)) {
			throw new NullPointerException("dto is empty");
		}
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(dto.getId());
		groupbuyingBoard.update(dto);

		fileService.updateFiles(dto, files, deletePhotoIdList, userId);
	}

	@Transactional
	public void order(Long userId, Long boardId) {
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);

		if (groupbuyingBoard.participateInDuplicate(userId)) {
			throw new DuplicateParticipationException();
		}
		if (groupbuyingBoard.isFull()) {
			throw new PersonnelLimitExceededException();
		}
		participantService.createAsParticipant(userId, groupbuyingBoard);
	}

	@Transactional
	public void cancel(Long userId, Long boardId) {
		ParticipantDTO dto = authentication.isParticipant(userId, boardId);

		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);

		if (groupbuyingBoard.getStatus() != Status.GATHERING) {
			throw new IllegalStateException();
		}

		participantService.delete(dto.getId());
	}

	@Transactional
	public List<Long> remove(Long userId, Long boardId) {

		authentication.isPromoter(userId, boardId);

		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);

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
			.orElseThrow(() -> new InvalidGroupbuyingBoardInstanceException());
	}

}