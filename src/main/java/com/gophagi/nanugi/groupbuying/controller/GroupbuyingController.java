package com.gophagi.nanugi.groupbuying.controller;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingThumbnailDTO;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardCommandService;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
public class GroupbuyingController {
	private final GroupbuyingBoardCommandService commandService;
	private final GroupbuyingBoardQueryService queryService;

	public GroupbuyingController(GroupbuyingBoardCommandService commandService,
		GroupbuyingBoardQueryService queryService) {
		this.commandService = commandService;
		this.queryService = queryService;
	}

	@PostMapping(value = "${groupbuying.create-url}",
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public void createV2(@RequestPart GroupbuyingBoardDTO dto,
		@RequestPart List<MultipartFile> files,
		HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		commandService.create(dto, files, userId);
	}

	//@PostMapping("${groupbuying.update-url}")
//	public void update(@RequestBody GroupbuyingBoardDTO dto) {
//		commandService.update(dto);
//	}

	@PostMapping(value ="${groupbuying.update-url}",
				 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public void updateV2(@RequestPart GroupbuyingBoardDTO dto,
										@RequestPart(required = false) List<MultipartFile> files,
										@RequestPart(required = false) List<Long> deletePhotoIdList,
										HttpSession session) {
		Long userId = (Long) session.getAttribute("id");
		commandService.update(dto, files,deletePhotoIdList, userId);

	}

	@PostMapping("${groupbuying.order-url}/{id}")
	public void order(@PathVariable("id") Long id, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		commandService.order(userId, id);
	}

	@PostMapping("${groupbuying.cancel-url}/{id}")
	public void cancel(@PathVariable("id") Long id, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		commandService.cancel(userId, id);
	}

	@GetMapping("${groupbuying.retrieve-url}/{id}")
	public GroupbuyingBoardDTO retrieve(@PathVariable("id") Long id) {
		commandService.updateView(id);
		return queryService.retrieve(id);
	}

	@GetMapping("${groupbuying.retrieve-list-url}/{page}")
	public Page<GroupbuyingThumbnailDTO> retrieveList(@PathVariable("page") int page) {
		return queryService.retrieveList(page);
	}

	@GetMapping("${groupbuying.retrieve-url}")
	public Page<GroupbuyingThumbnailDTO> retrieveCategoryList(Category category, int page) {
		return queryService.retrieveCategoryList(category, page);
	}
}
