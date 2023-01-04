package com.gophagi.nanugi.groupbuying.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.CannotDeleteBoardException;
import com.gophagi.nanugi.groupbuying.exception.DuplicateParticipationException;
import com.gophagi.nanugi.groupbuying.exception.InvalidGroupbuyingBoardInstanceException;
import com.gophagi.nanugi.groupbuying.exception.PersonnelLimitExceededException;
import com.gophagi.nanugi.groupbuying.repository.GroupbuyingBoardRepository;

@Service
public class GroupbuyingBoardCommandService {
	private final GroupbuyingBoardRepository repository;
	private final ParticipantService participantService;

	public GroupbuyingBoardCommandService(GroupbuyingBoardRepository repository,
		ParticipantService participantService) {
		this.repository = repository;
		this.participantService = participantService;
	}

	@Transactional
	public Long create(GroupbuyingBoardDTO dto, Long userId) {
		if (Objects.isNull(dto)) {
			throw new NullPointerException("dto is empty");
		}
		try {
			GroupbuyingBoard groupbuyingBoard = GroupbuyingBoard.toGroupbuyingBoard(dto);

			Long boardId = repository.save(groupbuyingBoard).getId();

			participantService.createAsPromoter(userId, groupbuyingBoard);

			return boardId;

		} catch (Exception exception) {
			throw new InvalidGroupbuyingBoardInstanceException();
		}
	}

	@Transactional
	public void update(GroupbuyingBoardDTO dto) {
		if (Objects.isNull(dto)) {
			throw new NullPointerException("dto is empty");
		}
		GroupbuyingBoard groupbuyingBoard = repository.findById(dto.getId())
			.orElseThrow(() -> new InvalidGroupbuyingBoardInstanceException());
		groupbuyingBoard.update(dto);
	}

	@Transactional
	public void order(Long userId, Long boardId) {
		GroupbuyingBoard groupbuyingBoard = repository.findById(boardId)
			.orElseThrow(() -> new InvalidGroupbuyingBoardInstanceException());

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
		List<ParticipantDTO> participants = participantService.retrieveByBoardId(boardId);
		for (ParticipantDTO participant : participants) {
			if (!isUser(participant, userId)) {
				continue;
			}
			Long participantId = participant.getId();
			switch (participant.getRole()) {
				case PARTICIPANT:
					participantService.delete(participantId);
					break;
				case PROMOTER:
					tryDeleteAsPromoter(participantId, boardId, participants);
					break;
			}
		}
	}

	private void delete(Long id) {
		repository.deleteById(id);
	}

	private void tryDeleteAsPromoter(Long participantId, Long boardId, List<ParticipantDTO> participants) {
		if (participants.size() > 1) {
			throw new CannotDeleteBoardException();
		}
		delete(boardId);
	}

	private boolean isUser(ParticipantDTO participant, Long userId) {
		return participant.getMember().getId().equals(userId);
	}
}