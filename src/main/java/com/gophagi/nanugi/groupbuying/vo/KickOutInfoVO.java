package com.gophagi.nanugi.groupbuying.vo;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class KickOutInfoVO {
	@NotNull(message = "board id is not null")
	private Long boardId;
	@NotNull(message = "participant id is not null")
	private Long kickedOutUserId;

	@Builder
	public KickOutInfoVO(Long boardId, Long kickedOutUserId) {
		this.boardId = boardId;
		this.kickedOutUserId = kickedOutUserId;
	}
}
