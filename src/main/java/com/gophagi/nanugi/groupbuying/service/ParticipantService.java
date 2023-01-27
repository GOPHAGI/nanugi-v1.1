package com.gophagi.nanugi.groupbuying.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.domain.Participant;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.InvalidParticipantInstanceException;
import com.gophagi.nanugi.groupbuying.repository.ParticipantRepository;
import com.gophagi.nanugi.member.domain.Member;
import com.gophagi.nanugi.member.exception.InvalidMemberInstanceException;
import com.gophagi.nanugi.member.repository.MemberRepository;

@Service
public class ParticipantService {
	private final ParticipantRepository repository;
	private final MemberRepository memberRepository;

	public ParticipantService(ParticipantRepository repository, MemberRepository memberRepository) {
		this.repository = repository;
		this.memberRepository = memberRepository;
	}

	public void createAsPromoter(Long userId, GroupbuyingBoard groupbuyingBoard) {
		// 사용자 정보 가져오기
		Member user = getUserById(userId);
		// 참여자(게시자) 정보 생성
		Participant participant = Participant.builder()
			.member(user)
			.groupbuyingBoard(groupbuyingBoard)
			.role(Role.PROMOTER)
			.build();
		try {
			// 참여자(게시자) 정보 저장
			repository.save(participant);
		} catch (IllegalArgumentException exception) {
			throw new InvalidParticipantInstanceException(ErrorCode.INSERT_ERROR);
		}
	}

	public void createAsParticipant(Long userId, GroupbuyingBoard groupbuyingBoard) {
		// 사용자 정보 가져오기
		Member user = getUserById(userId);
		// 참여자 정보 생성
		Participant participant = Participant.builder()
			.member(user)
			.groupbuyingBoard(groupbuyingBoard)
			.role(Role.PARTICIPANT)
			.build();
		try {
			// 참여자 정보 저장
			repository.save(participant);
		} catch (IllegalArgumentException exception) {
			throw new InvalidParticipantInstanceException(ErrorCode.INSERT_ERROR);
		}
	}

	public List<ParticipantDTO> retrieveByUserId(Long userId) {
		List<Participant> participants = getParticipantsByMemberId(userId);
		return ParticipantDTO.toParticipantDTOs(participants);
	}

	public List<ParticipantDTO> retrieveByUserIdAndRole(Long userId, Role role) {
		List<Participant> participants = getParticipantsByMemberIdAndRole(userId, role);
		return ParticipantDTO.toParticipantDTOs(participants);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Transactional
	public ParticipantDTO retrieveByUserIdAndBoardId(Long userId, Long boardId) throws
		InvalidParticipantInstanceException {
		Participant participant = getParticipantByMemberIdAndBoardId(userId, boardId);
		return ParticipantDTO.toParticipantDTO(participant);
	}

	private Member getUserById(Long userId) {
		return memberRepository.findById(userId)
			.orElseThrow(() -> new InvalidMemberInstanceException(ErrorCode.RETRIEVE_ERROR));
	}

	private List<Participant> getParticipantsByMemberId(Long userId) {
		return repository.findByMemberId(userId)
			.orElseThrow(() -> new InvalidParticipantInstanceException(ErrorCode.RETRIEVE_ERROR));
	}

	private Participant getParticipantByMemberIdAndBoardId(Long userId, Long boardId) {
		return repository.findByGroupbuyingBoardIdAndMemberId(boardId, userId)
			.orElseThrow(() -> new InvalidParticipantInstanceException(ErrorCode.RETRIEVE_ERROR));
	}

	private List<Participant> getParticipantsByMemberIdAndRole(Long userId, Role role) {
		return repository.findByMemberIdAndRole(userId, role)
			.orElseThrow(() -> new InvalidParticipantInstanceException(ErrorCode.RETRIEVE_ERROR));
	}
}
