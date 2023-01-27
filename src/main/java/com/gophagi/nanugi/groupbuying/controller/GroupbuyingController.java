package com.gophagi.nanugi.groupbuying.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingThumbnailDTO;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardCommandService;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;

import lombok.extern.slf4j.Slf4j;

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

	@PostMapping(value = "${groupbuying.create-url}")
	public void create(@RequestBody GroupbuyingBoardDTO dto, @CookieValue String token) {

		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.create(dto, userId);
	}

	/* json -> object deserializer 구현해야됨.

	@PostMapping(value = "${groupbuying.update-url}")
	public void update(@RequestBody GroupbuyingBoardUpdateDTO dto, @CookieValue String token) {

		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.update(dto, userId);
	}

	*/

	@PostMapping("${groupbuying.order-url}/{id}")
	public void order(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.order(userId, id);
	}

	@PostMapping("${groupbuying.cancel-url}/{id}")
	public void cancel(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.cancel(userId, id);
	}

	@PostMapping("${groupbuying.remove-url}/{id}")
	public List<Long> remove(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		return commandService.remove(userId, id);
	}

	@PostMapping("${groupbuying.progress-url}/{id}")
	public void progress(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.progress(userId, id);
	}

	@PostMapping("${groupbuying.deprogress-url}/{id}")
	public void deprogress(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = Long.parseLong(JwtTokenProvider.getUserNameFromJwt(token));
		commandService.deprogress(userId, id);
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
