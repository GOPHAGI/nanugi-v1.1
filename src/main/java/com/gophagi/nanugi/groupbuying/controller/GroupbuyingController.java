package com.gophagi.nanugi.groupbuying.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.constant.Status;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardCommandService;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;
import com.gophagi.nanugi.groupbuying.vo.BoardIdAndTitleVO;
import com.gophagi.nanugi.groupbuying.vo.GroupbuyingBoardInsertVO;
import com.gophagi.nanugi.groupbuying.vo.GroupbuyingBoardUpdateVO;
import com.gophagi.nanugi.groupbuying.vo.GroupbuyingThumbnailVO;
import com.gophagi.nanugi.groupbuying.vo.KickOutInfoVO;
import com.gophagi.nanugi.groupbuying.vo.RemoveInfoVO;

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
	public BoardIdAndTitleVO create(@RequestBody GroupbuyingBoardInsertVO vo, @CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return commandService.create(vo.toGroupbuyingBoardDTO(), userId);
	}

	@PostMapping(value = "${groupbuying.update-url}")
	public Long update(@Valid @RequestBody GroupbuyingBoardUpdateVO vo, @CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return commandService.update(vo, userId);
	}

	@PostMapping("${groupbuying.order-url}/{id}")
	public Long order(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return commandService.order(userId, id);
	}

	@PostMapping("${groupbuying.cancel-url}/{id}")
	public void cancel(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		commandService.cancel(userId, id);
	}

	@PostMapping("${groupbuying.remove-url}/{id}")
	public RemoveInfoVO remove(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return commandService.remove(userId, id);
	}

	@PostMapping("${groupbuying.progress-url}/{id}")
	public Status progress(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return commandService.progress(userId, id);
	}

	@PostMapping("${groupbuying.deprogress-url}/{id}")
	public Status deprogress(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return commandService.deprogress(userId, id);
	}

	@PostMapping("${groupbuying.complete-url}/{id}")
	public Status complete(@PathVariable("id") Long id, @CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return commandService.complete(userId, id);
	}

	@PostMapping("${groupbuying.kickout-url}/{id}")
	public KickOutInfoVO kickOut(@PathVariable("id") Long boardId, @RequestParam Long participantId,
		@CookieValue String token) {
		Long userId = JwtTokenProvider.getUserIdFromJwt(token);
		return commandService.kickOut(userId, boardId, participantId);
	}

	@GetMapping("${groupbuying.retrieve-url}/{id}")
	public GroupbuyingBoardDTO retrieve(@PathVariable("id") Long id) {
		commandService.updateView(id);
		return queryService.retrieve(id);
	}

	@GetMapping("${groupbuying.retrieve-list-url}/{page}")
	public Page<GroupbuyingThumbnailVO> retrieveList(@PathVariable("page") int page) {
		return queryService.retrieveList(page);
	}

	@GetMapping("${groupbuying.retrieve-url}")
	public Page<GroupbuyingThumbnailVO> retrieveCategoryList(@RequestParam Category category, @RequestParam int page) {
		return queryService.retrieveCategoryList(category, page);
	}
}
