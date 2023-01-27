package com.gophagi.nanugi.groupbuying.vo;

import java.util.List;
import java.util.stream.Collectors;

import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.constant.Status;
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
public class GroupbuyingThumbnailVO {
	private Long id;
	private PhotoDTO photo;
	private String title;
	private Category category;
	private Status status;
	private Integer price;
	private String regionCode;
	private String wishLocationAddress;
	private int limitedNumberOfParticipants;
	private int numberOfParticipants;

	@Builder
	public GroupbuyingThumbnailVO(Long id, PhotoDTO photo, String title, Category category, Status status,
		Integer price, String regionCode, String wishLocationAddress, int limitedNumberOfParticipants,
		int numberOfParticipants) {
		this.id = id;
		this.photo = photo;
		this.title = title;
		this.category = category;
		this.status = status;
		this.price = price;
		this.regionCode = regionCode;
		this.wishLocationAddress = wishLocationAddress;
		this.limitedNumberOfParticipants = limitedNumberOfParticipants;
		this.numberOfParticipants = numberOfParticipants;
	}

	public static GroupbuyingThumbnailVO toGroupbuyingThumbnailVO(GroupbuyingBoard groupbuyingBoard) {
		return GroupbuyingThumbnailVO.builder()
			.id(groupbuyingBoard.getId())
			.photo(PhotoDTO.toPhotoDTO(groupbuyingBoard.getPhotos().get(0)))
			.title(groupbuyingBoard.getTitle())
			.category(groupbuyingBoard.getCategory())
			.status(groupbuyingBoard.getStatus())
			.price(groupbuyingBoard.getPrice())
			.regionCode(groupbuyingBoard.getRegionCode())
			.wishLocationAddress(groupbuyingBoard.getWishLocationAddress())
			.limitedNumberOfParticipants(groupbuyingBoard.getLimitedNumberOfParticipants())
			.numberOfParticipants(groupbuyingBoard.getParticipants().size())
			.build();
	}

	public static List<GroupbuyingThumbnailVO> toGroupbuyingThumbnailVOs(List<GroupbuyingBoard> groupbuyingBoards) {
		return groupbuyingBoards.stream()
			.map(GroupbuyingThumbnailVO::toGroupbuyingThumbnailVO)
			.collect(Collectors.toList());
	}

}
