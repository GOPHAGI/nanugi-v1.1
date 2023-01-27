package com.gophagi.nanugi.groupbuying.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gophagi.nanugi.common.auth.CommonAuthentication;
import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.common.util.file.domain.Photo;
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
import com.gophagi.nanugi.groupbuying.vo.BoardIdAndTitleVO;
import com.gophagi.nanugi.groupbuying.vo.GroupbuyingBoardUpdateVO;
import com.gophagi.nanugi.groupbuying.vo.KickOutInfoVO;
import com.gophagi.nanugi.groupbuying.vo.RemoveInfoVO;
import com.gophagi.nanugi.member.domain.Member;

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
	public BoardIdAndTitleVO create(GroupbuyingBoardDTO dto, Long userId) {
		try {
			// 공동구매 게시글 생성
			GroupbuyingBoard groupbuyingBoard = GroupbuyingBoard.toGroupbuyingBoard(dto);
			repository.save(groupbuyingBoard);
			// 이미지 파일 저장 (공동구매 - 이미지 파일)
			fileService.saveAllPhotos(Photo.findAndSetNewPhotos(dto, groupbuyingBoard));
			// 게시자로 공동구매 참여자 목록 추가
			participantService.createAsPromoter(userId, groupbuyingBoard);

			return BoardIdAndTitleVO.toBoardIdAndTitleVO(groupbuyingBoard);

		} catch (Exception exception) {
			throw new InvalidGroupbuyingBoardInstanceException(ErrorCode.INSERT_ERROR);
		}
	}

	@Transactional
	public Long update(GroupbuyingBoardUpdateVO vo, Long userId) {
		// 공동구매 수정 권한 확인 (게시자만 수정 가능)
		authentication.isPromoter(userId, vo.getId());
		// 공동구매의 진행상태에 따른 수정 가능 여부 확인
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(vo.getId());
		if (groupbuyingBoard.getStatus() != Status.GATHERING) {
			throw new IllegalStateException(ErrorCode.CANNOT_UPDATE_BOARD.getMessage());
		}
		// 기존 이미지 리스트에서 삭제될 이미지 처리
		groupbuyingBoard.deletePhoto(vo.getDeletePhotoIdList());
		// 공동구매 변경내역 업데이트
		groupbuyingBoard.update(vo.toGroupbuyingBoardDTO());
		// 새로운 이미지 파일 저장 (공동구매 - 이미지 파일)
		fileService.saveAllPhotos(Photo.findAndSetNewPhotos(vo.toGroupbuyingBoardDTO(), groupbuyingBoard));

		return groupbuyingBoard.getId();
	}

	@Transactional
	public Long order(Long userId, Long boardId) {
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);

		// 공동구매의 진행상태에 따른 신청 가능 여부 확인
		if (groupbuyingBoard.getStatus() != Status.GATHERING) {
			throw new IllegalStateException();
		}

		// 중복 요청 시 예외처리
		if (groupbuyingBoard.participateInDuplicate(userId)) {
			throw new DuplicateParticipationException(ErrorCode.PARTICIPATION_DUPLICATION);
		}
		// 공동구매 제한 인원 초과 시 예외처리
		if (groupbuyingBoard.isFull()) {
			throw new PersonnelLimitExceededException(ErrorCode.PERSONNEL_LIMIT_EXCEEDED);
		}
		// 공동구매 참여자 등록
		participantService.createAsParticipant(userId, groupbuyingBoard);

		return groupbuyingBoard.getId();
	}

	@Transactional
	public void cancel(Long userId, Long boardId) {
		// 공동구매에 대한 참여자 여부 확인
		ParticipantDTO dto = authentication.isParticipant(userId, boardId);
		// 공동구매의 진행상태에 따른 취소 가능 여부 확인
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);
		if (groupbuyingBoard.getStatus() != Status.GATHERING) {
			throw new IllegalStateException(ErrorCode.CANNOT_CANCEL_REQUEST.getMessage());
		}
		// 공동구매 참여자에서 제외 -> 취소 진행
		participantService.deleteByMemberId(dto.getMember().getId());
	}

	@Transactional
	public RemoveInfoVO remove(Long userId, Long boardId) {
		// 공동구매에 대한 게시자 여부 확인
		authentication.isPromoter(userId, boardId);
		// 공동구매의 진행상태에 따른 삭제 가능 여부 확인
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);
		if (groupbuyingBoard.getStatus() == Status.DONE) {
			throw new IllegalStateException(ErrorCode.CANNOT_DELETE_BOARD.getMessage());
		}
		// 채팅방 삭제를 위해 참여자 id 리스트 반환
		List<Long> participantUserIds = groupbuyingBoard.getParticipants()
			.stream()
			.map(Participant::getMember)
			.map(Member::getId)
			.collect(Collectors.toList());
		// 공동구매 게시글 삭제 진행
		repository.delete(groupbuyingBoard);

		return new RemoveInfoVO(boardId, participantUserIds);
	}

	@Transactional
	public void updateView(Long boardId) {
		// 공동구매 게시글 조회수 증가
		repository.updateView(boardId);
	}

	private GroupbuyingBoard getGroupbuyingBoard(Long boardId) {
		return repository.findById(boardId)
			.orElseThrow(() -> new InvalidGroupbuyingBoardInstanceException(ErrorCode.RETRIEVE_ERROR));
	}

	@Transactional
	public Status progress(Long userId, Long boardId) {
		// 공동구매 진행 권한 확인 (게시자만 가능)
		authentication.isPromoter(userId, boardId);
		// 공동구매의 진행상태에 따른 진행 가능 여부 확인
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);
		if (groupbuyingBoard.getStatus() != Status.GATHERING) {
			throw new IllegalStateException();
		}
		// 공동구매의 진행상태를 ONGOING으로 변경
		groupbuyingBoard.updateStatus(Status.ONGOING);

		return Status.ONGOING;
	}

	@Transactional
	public Status deprogress(Long userId, Long boardId) {
		// 공동구매 진행 취소 권한 확인 (게시자만 가능)
		authentication.isPromoter(userId, boardId);
		// 공동구매의 진행상태에 따른 진행 취소 가능 여부 확인
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);
		if (groupbuyingBoard.getStatus() != Status.ONGOING) {
			throw new IllegalStateException();
		}
		// 공동구매의 진행상태를 GATHERING으로 변경
		groupbuyingBoard.updateStatus(Status.GATHERING);

		return Status.GATHERING;
	}

	@Transactional
	public Status complete(Long userId, Long boardId) {
		// 공동구매 완료 권한 확인 (게시자만 가능)
		authentication.isPromoter(userId, boardId);
		// 공동구매의 진행상태에 따른 진행 취소 가능 여부 확인
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);
		if (groupbuyingBoard.getStatus() == Status.DONE) {
			throw new IllegalStateException();
		}
		// 공동구매의 진행상태를 DONE으로 변경
		groupbuyingBoard.updateStatus(Status.DONE);

		return Status.DONE;
	}

	@Transactional
	public KickOutInfoVO kickOut(Long userId, Long boardId, Long kickedOutUserId) {
		// 공동구매 강퇴 권한 확인 (게시자만 가능)
		authentication.isPromoter(userId, boardId);
		authentication.isParticipant(kickedOutUserId, boardId);
		// 공동구매의 진행상태에 따른 강퇴 가능 여부 확인
		GroupbuyingBoard groupbuyingBoard = getGroupbuyingBoard(boardId);
		if (groupbuyingBoard.getStatus() != Status.GATHERING) {
			throw new IllegalStateException();
		}
		// participant 강퇴
		participantService.deleteByMemberId(kickedOutUserId);

		return new KickOutInfoVO(boardId, kickedOutUserId);
	}
}