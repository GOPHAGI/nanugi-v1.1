package com.gophagi.nanugi.groupbuying.vo;

import java.util.List;

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
public class RemoveInfoVO {
	@NotNull(message = "board id is not null")
	private Long boardId;
	@NotNull(message = "participant id is not null")
	private List<Long> participantIds;

	@Builder
	public RemoveInfoVO(Long boardId, List<Long> participantIds) {
		this.boardId = boardId;
		this.participantIds = participantIds;
	}
}
