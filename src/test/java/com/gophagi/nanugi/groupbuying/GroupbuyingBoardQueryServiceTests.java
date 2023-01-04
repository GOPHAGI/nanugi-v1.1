package com.gophagi.nanugi.groupbuying;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gophagi.nanugi.groupbuying.exception.InvalidGroupbuyingBoardInstanceException;
import com.gophagi.nanugi.groupbuying.repository.GroupbuyingBoardRepository;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;

@ExtendWith(MockitoExtension.class)
public class GroupbuyingBoardQueryServiceTests {
	@Mock
	GroupbuyingBoardRepository repository;
	@InjectMocks
	GroupbuyingBoardQueryService service;

	@Test
	void 보드가_존재하지_않을_때() {
		when(repository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
		assertThatThrownBy(() -> service.retrieve(1l)).isInstanceOf(InvalidGroupbuyingBoardInstanceException.class);
	}

}
