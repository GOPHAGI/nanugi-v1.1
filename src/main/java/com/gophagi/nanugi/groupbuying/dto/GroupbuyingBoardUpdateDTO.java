package com.gophagi.nanugi.groupbuying.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupbuyingBoardUpdateDTO extends GroupbuyingBoardDTO {
	private List<Long> deletePhotoIdList;
}
