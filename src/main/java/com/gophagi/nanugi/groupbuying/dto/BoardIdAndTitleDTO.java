package com.gophagi.nanugi.groupbuying.dto;

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
public class BoardIdAndTitleDTO {

	@NotNull(message = "id is not null")
	private Long id;

	@NotBlank(message = "title is not blank")
	private String title;

	@Builder
	public BoardIdAndTitleDTO(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public static BoardIdAndTitleDTO toGroupbuyingBoardDTO(GroupbuyingBoard groupbuyingBoard) {
		return BoardIdAndTitleDTO.builder()
			.id(groupbuyingBoard.getId())
			.title(groupbuyingBoard.getTitle())
			.build();
	}
}
