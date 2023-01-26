package com.gophagi.nanugi.groupbuying.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.domain.Participant;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.InvalidParticipantInstanceException;
import com.gophagi.nanugi.groupbuying.repository.ParticipantRepository;
import com.gophagi.nanugi.member.domain.Member;
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
		Member user = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("invalid user"));
		Participant participant = Participant.builder()
			.member(user)
			.groupbuyingBoard(groupbuyingBoard)
			.role(Role.PROMOTER)
			.build();
		try {
			repository.save(participant);
		} catch (Exception exception) {
			throw new InvalidParticipantInstanceException();
		}
	}

	public void createAsParticipant(Long userId, GroupbuyingBoard groupbuyingBoard) {
		Member user = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("invalid user"));
		Participant participant = Participant.builder()
			.member(user)
			.groupbuyingBoard(groupbuyingBoard)
			.role(Role.PARTICIPANT)
			.build();
		repository.save(participant);
	}

	public List<ParticipantDTO> retrieveByBoardId(Long boardId) {
		List<Participant> participants = repository.findByGroupbuyingBoardId(boardId)
			.orElseThrow(() -> new InvalidParticipantInstanceException());

		List<ParticipantDTO> participantDTOS = new ArrayList<>();
		for (Participant participant : participants) {
			participantDTOS.add(ParticipantDTO.toParticipantDTO(participant));
		}
		return participantDTOS;
	}

	public List<ParticipantDTO> retrieveByUserId(Long userId) {
		List<Participant> participants = repository.findByMemberId(userId)
			.orElseThrow(() -> new InvalidParticipantInstanceException());

		List<ParticipantDTO> participantDTOS = new ArrayList<>();
		for (Participant participant : participants) {
			participantDTOS.add(ParticipantDTO.toParticipantDTO(participant));
		}
		return participantDTOS;
	}

	public List<ParticipantDTO> retrieveByUserIdAndRole(Long userId, Role role) {
		List<Participant> participants = repository.findByMemberIdAndRole(userId, role)
			.orElseThrow(() -> new InvalidParticipantInstanceException());

		List<ParticipantDTO> participantDTOS = new ArrayList<>();
		for (Participant participant : participants) {
			participantDTOS.add(ParticipantDTO.toParticipantDTO(participant));
		}
		return participantDTOS;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Transactional
	public ParticipantDTO retrieveByUserIdAndBoardId(Long userId, Long boardId) {
		Participant participant = repository.findByGroupbuyingBoardIdAndMemberId(boardId, userId)
			.orElseThrow(() -> new RuntimeException("해당 보드와 사용자 아이디가 일치하는 데이터가 존재하지 않음."));
		return ParticipantDTO.toParticipantDTO(participant);
	}
}
