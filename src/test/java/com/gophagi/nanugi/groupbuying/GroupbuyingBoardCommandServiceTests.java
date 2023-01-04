package com.gophagi.nanugi.groupbuying;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.domain.Participant;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.CannotDeleteBoardException;
import com.gophagi.nanugi.groupbuying.exception.DuplicateParticipationException;
import com.gophagi.nanugi.groupbuying.exception.InvalidGroupbuyingBoardInstanceException;
import com.gophagi.nanugi.groupbuying.exception.InvalidParticipantInstanceException;
import com.gophagi.nanugi.groupbuying.exception.PersonnelLimitExceededException;
import com.gophagi.nanugi.groupbuying.repository.GroupbuyingBoardRepository;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardCommandService;
import com.gophagi.nanugi.groupbuying.service.ParticipantService;
import com.gophagi.nanugi.member.domain.Member;

@ExtendWith(MockitoExtension.class)
public class GroupbuyingBoardCommandServiceTests {
	@Mock
	GroupbuyingBoardRepository repository;
	@Mock
	ParticipantService participantService;
	@InjectMocks
	GroupbuyingBoardCommandService service;

	@Test
	void 공동구매_매개변수가_null_일_때() {
		assertThatThrownBy(() -> service.create(null, 1L)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void 공동구매보드_insert_실패() {
		when(repository.save(ArgumentMatchers.any())).thenThrow(new RuntimeException());
		assertThatThrownBy(() -> service.create(new GroupbuyingBoardDTO(), 1L)).isInstanceOf(
			InvalidGroupbuyingBoardInstanceException.class);
	}

	@Test
	void 중복참여() {
		GroupbuyingBoard groupbuyingBoard = GroupbuyingBoard.builder()
			.participants(List.of(Participant.builder().member(Member.builder().id(1L).build()).build()))
			.build();

		when(repository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(groupbuyingBoard));
		assertThatThrownBy(() -> service.order(1L, 1L)).isInstanceOf(DuplicateParticipationException.class);
	}

	@Test
	void 정원초과() {
		GroupbuyingBoard groupbuyingBoard = GroupbuyingBoard.builder()
			.participants(List.of(Participant.builder().member(Member.builder().id(2L).build()).build()))
			.limitedNumberOfParticipants(1)
			.build();

		when(repository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(groupbuyingBoard));
		assertThatThrownBy(() -> service.order(1L, 1L)).isInstanceOf(PersonnelLimitExceededException.class);
	}

	@Test
	void 유효하지_않은_보드에_대한_취소_혹은_삭제_요청() {
		when(participantService.retrieveByBoardId(ArgumentMatchers.any())).thenThrow(
			InvalidParticipantInstanceException.class);
		assertThatThrownBy(() -> service.cancel(1L, 1L)).isInstanceOf(InvalidParticipantInstanceException.class);
	}

	@Test
	void 참여자_존재하는_보드에_대한_삭제_요청_시() {
		Member user = Member.builder().id(1L).build();
		ParticipantDTO participant = ParticipantDTO.builder().role(Role.PROMOTER).member(user).build();
		Member user2 = Member.builder().id(2L).build();
		ParticipantDTO participant2 = ParticipantDTO.builder().role(Role.PROMOTER).member(user2).build();

		when(participantService.retrieveByBoardId(ArgumentMatchers.any())).thenReturn(
			List.of(participant, participant2));
		assertThatThrownBy(() -> service.cancel(1L, 1L)).isInstanceOf(CannotDeleteBoardException.class);
	}
}
