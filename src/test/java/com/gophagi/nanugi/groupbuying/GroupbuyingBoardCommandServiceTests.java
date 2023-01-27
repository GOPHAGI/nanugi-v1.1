package com.gophagi.nanugi.groupbuying;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.gophagi.nanugi.common.auth.CommonAuthentication;
import com.gophagi.nanugi.common.util.file.service.FileService;
import com.gophagi.nanugi.groupbuying.constant.Status;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.domain.Participant;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.groupbuying.exception.DuplicateParticipationException;
import com.gophagi.nanugi.groupbuying.exception.InvalidGroupbuyingBoardInstanceException;
import com.gophagi.nanugi.groupbuying.exception.NoAuthorityException;
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
	@Mock
	CommonAuthentication authentication;
	@Mock
	FileService fileService;
	@InjectMocks
	GroupbuyingBoardCommandService service;

	@Test
	void 공동구매_매개변수가_null_일_때() {
		List<MultipartFile> files = new ArrayList<>();
		assertThatThrownBy(() -> service.create(null, 1L)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void 공동구매보드_insert_실패() {
		List<MultipartFile> files = new ArrayList<>();
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
	void 유효하지_않은_보드에_대한_취소_요청() {
		when(authentication.isParticipant(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(
			NoAuthorityException.class);
		assertThatThrownBy(() -> service.cancel(1L, 1L)).isInstanceOf(NoAuthorityException.class);
	}

	@Test
	void 유효하지_않은_상태에_대한_취소_요청() {
		when(authentication.isParticipant(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(
			ParticipantDTO.builder().id(1L).build());
		when(repository.findById(1L)).thenReturn(
			Optional.of(GroupbuyingBoard.builder().id(1L).status(Status.ONGOING).build()));
		assertThatThrownBy(() -> service.cancel(1L, 1L)).isInstanceOf(IllegalStateException.class);
	}

	@Test
	void 유효하지_않은_상태에_대한_삭제_요청_시() {
		when(repository.findById(1L)).thenReturn(Optional.of(GroupbuyingBoard.builder().id(1L).status(Status.DONE)
			.participants(List.of(Participant.builder().id(1L).build()))
			.build()));

		assertThatThrownBy(() -> service.remove(1L, 1L)).isInstanceOf(IllegalStateException.class);
	}
}
