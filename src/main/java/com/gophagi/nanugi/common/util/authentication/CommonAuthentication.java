package com.gophagi.nanugi.common.util.authentication;

import org.springframework.stereotype.Component;

import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.NoAuthorityException;
import com.gophagi.nanugi.groupbuying.service.ParticipantService;

@Component
public class CommonAuthentication {
	private final ParticipantService participantService;

	public CommonAuthentication(ParticipantService participantService) {
		this.participantService = participantService;
	}

	public void hasAuthority(Long userId, Long boardId) {
		try {

			participantService.retrieveByUserIdAndBoardId(userId, boardId);

		} catch (RuntimeException e) {

			throw new NoAuthorityException();

		}
	}

	public void isPromoter(Long userId, Long boardId) {

		ParticipantDTO participant = participantService.retrieveByUserIdAndBoardId(userId, boardId);

		if (Role.PROMOTER == participant.getRole()) {
			return;
		}

		throw new NoAuthorityException();
	}

	public ParticipantDTO isParticipant(Long userId, Long boardId) {

		ParticipantDTO participant = participantService.retrieveByUserIdAndBoardId(userId, boardId);

		if (Role.PARTICIPANT == participant.getRole()) {
			return participant;
		}

		throw new NoAuthorityException();
	}

}
