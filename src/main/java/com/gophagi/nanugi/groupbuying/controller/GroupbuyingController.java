package com.gophagi.nanugi.groupbuying.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardCommandService;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;

@RestController
public class GroupbuyingController {
	private final GroupbuyingBoardCommandService commandService;
	private final GroupbuyingBoardQueryService queryService;

	public GroupbuyingController(GroupbuyingBoardCommandService commandService,
		GroupbuyingBoardQueryService queryService) {
		this.commandService = commandService;
		this.queryService = queryService;
	}

	@PostMapping("${groupbuying.create-url}")
	public GroupbuyingBoardDTO create(@RequestBody GroupbuyingBoardDTO dto, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		Long boardId = commandService.create(dto, userId);
		return queryService.retrieve(boardId);
	}

	@PostMapping("${groupbuying.update-url}")
	public GroupbuyingBoardDTO update(@RequestBody GroupbuyingBoardDTO dto) {
		commandService.update(dto);
		return queryService.retrieve(dto.getId());
	}

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
