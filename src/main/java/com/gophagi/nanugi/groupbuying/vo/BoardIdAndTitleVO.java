package com.gophagi.nanugi.groupbuying.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardIdAndTitleVO {

	@NotNull(message = "id is not null")
	private Long id;

	@NotBlank(message = "title is not blank")
	private String title;

	@Builder
	public BoardIdAndTitleVO(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public static BoardIdAndTitleVO toBoardIdAndTitleVO(GroupbuyingBoard groupbuyingBoard) {
		return BoardIdAndTitleVO.builder()
			.id(groupbuyingBoard.getId())
			.title(groupbuyingBoard.getTitle())
			.build();
	}
}
