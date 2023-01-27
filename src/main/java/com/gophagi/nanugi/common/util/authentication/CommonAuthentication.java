package com.gophagi.nanugi.common.util.authentication;

import org.springframework.stereotype.Component;

import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.InvalidParticipantInstanceException;
import com.gophagi.nanugi.groupbuying.exception.NoAuthorityException;
import com.gophagi.nanugi.groupbuying.service.ParticipantService;

@Component
public class CommonAuthentication {
	private final ParticipantService participantService;

	public CommonAuthentication(ParticipantService participantService) {
		this.participantService = participantService;
	}

	public ParticipantDTO hasAuthority(Long userId, Long boardId) {
		try {
			// 공동구매 참여자(게시자 포함) 여부 확인
			return participantService.retrieveByUserIdAndBoardId(userId, boardId);
		} catch (InvalidParticipantInstanceException e) {
			throw new NoAuthorityException(ErrorCode.RETRIEVE_ERROR);
		}
	}

	public void isPromoter(Long userId, Long boardId) {

		ParticipantDTO participant = hasAuthority(userId, boardId);

		if (Role.PROMOTER == participant.getRole()) {
			return;
		}

		throw new NoAuthorityException(ErrorCode.NOT_PROMOTER);
	}

	public ParticipantDTO isParticipant(Long userId, Long boardId) {

		ParticipantDTO participant = hasAuthority(userId, boardId);

		if (Role.PARTICIPANT == participant.getRole()) {
			return participant;
		}

		throw new NoAuthorityException(ErrorCode.NOT_PARTICIPANT);
	}

}
