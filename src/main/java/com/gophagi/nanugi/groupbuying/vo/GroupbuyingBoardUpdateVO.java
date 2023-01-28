package com.gophagi.nanugi.groupbuying.vo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.validator.EnumValid;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupbuyingBoardUpdateVO {
	@NotNull(message = "id is not blank")
	private Long id;
	@NotBlank(message = "title is not blank")
	@Size(max = 255, message = "length is less than 255")
	private String title;
	@EnumValid(message = "category is not null")
	private Category category;
	@NotNull(message = "price is not blank")
	@PositiveOrZero
	private Integer price;
	@NotBlank(message = "url is not blank")
	@URL(message = "it's not a valid URL")
	private String url;
	@NotBlank(message = "region code is not blank")
	private String regionCode;
	private String wishLocationAddress;
	@NotNull(message = "limited number of participants is not blank")
	@Positive
	private Integer limitedNumberOfParticipants;
	@NotBlank(message = "description is not blank")
	private String description;
	@NotNull(message = "view count is not blank")
	private Integer viewCount;
	@NotEmpty(message = "photos is not empty")
	@Valid
	private List<PhotoDTO> photos;
	private List<Long> deletePhotoIdList;

	@Builder
	public GroupbuyingBoardUpdateVO(Long id, String title, Category category, Integer price, String url,
		String regionCode, String wishLocationAddress, Integer limitedNumberOfParticipants, String description,
		Integer viewCount, List<PhotoDTO> photos, List<Long> deletePhotoIdList) {
		this.id = id;
		this.title = title;
		this.category = category;
		this.price = price;
		this.url = url;
		this.regionCode = regionCode;
		this.wishLocationAddress = wishLocationAddress;
		this.limitedNumberOfParticipants = limitedNumberOfParticipants;
		this.description = description;
		this.viewCount = viewCount;
		this.photos = photos;
		this.deletePhotoIdList = deletePhotoIdList;
	}

	public GroupbuyingBoardDTO toGroupbuyingBoardDTO() {
		return GroupbuyingBoardDTO.builder()
			.id(id)
			.title(title)
			.category(category)
			.price(price)
			.url(url)
			.regionCode(regionCode)
			.wishLocationAddress(wishLocationAddress)
			.limitedNumberOfParticipants(limitedNumberOfParticipants)
			.description(description)
			.viewCount(viewCount)
			.photos(photos)
			.build();
	}
}
