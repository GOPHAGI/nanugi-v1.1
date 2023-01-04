package com.gophagi.nanugi.groupbuying.controller;

import javax.servlet.http.HttpSession;

import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.common.util.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardCommandService;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
public class GroupbuyingController {
	private final GroupbuyingBoardCommandService commandService;
	private final GroupbuyingBoardQueryService queryService;
	private final FileService fileService;

	public GroupbuyingController(GroupbuyingBoardCommandService commandService,
								 GroupbuyingBoardQueryService queryService, FileService fileService) {
		this.commandService = commandService;
		this.queryService = queryService;
		this.fileService = fileService;
	}

	//@PostMapping("${groupbuying.create-url}")
	public GroupbuyingBoardDTO create(@RequestBody GroupbuyingBoardDTO dto, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		Long boardId = commandService.create(dto, userId);
		return queryService.retrieve(boardId);
	}

	@PostMapping(value = "${groupbuying.create-url}",
				 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public GroupbuyingBoardDTO createV2(@RequestPart GroupbuyingBoardDTO dto,
										@RequestPart List<MultipartFile> files,
										HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		Long boardId = commandService.create(dto, userId);
		GroupbuyingBoardDTO retiveBoard = queryService.retrieve(boardId);
		List<PhotoDTO> uploadItemsList  = fileService.saveFiles(userId, retiveBoard, files);
		return retiveBoard;
	}

	@PostMapping("${groupbuying.update-url}")
	public GroupbuyingBoardDTO update(@RequestBody GroupbuyingBoardDTO dto) {
		commandService.update(dto);
		return queryService.retrieve(dto.getId());
	}

	//@PostMapping(value ="${groupbuying.update-url}",
	//			 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
//	public GroupbuyingBoardDTO updateV2(@RequestPart GroupbuyingBoardDTO dto,
//										@RequestPart List<MultipartFile> files) {
//		commandService.update(dto);
//		GroupbuyingBoardDTO retiveBoard = queryService.retrieve(dto.getId());
//		//todo: 이미지 업데이트
//
//		return retiveBoard;
//	}

	@PostMapping("${groupbuying.order-url}/{id}")
	public GroupbuyingBoardDTO order(@PathVariable("id") Long id, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		commandService.order(userId, id);
		return queryService.retrieve(id);
	}

	@PostMapping("${groupbuying.cancel-url}/{id}")
	public void cancel(@PathVariable("id") Long id, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		commandService.cancel(userId, id);
	}

	@GetMapping("${groupbuying.retrieve-url}/{id}")
	public GroupbuyingBoardDTO retrieve(@PathVariable("id") Long id) {
		return queryService.retrieve(id);
	}
}
