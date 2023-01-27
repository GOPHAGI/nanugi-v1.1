package com.gophagi.nanugi.groupbuying.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.constant.Status;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.validation.Insert;
import com.gophagi.nanugi.groupbuying.validation.Update;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupbuyingBoardDTO {
	@NotNull(message = "id is not null", groups = Update.class)
	private Long id;
	@NotBlank(message = "title is not blank", groups = {Insert.class, Update.class})
	@Size(max = 255, message = "length is less than 255")
	private String title;
	@NotBlank(message = "category is not blank", groups = {Insert.class, Update.class})
	private Category category;
	@NotBlank(message = "status is not blank", groups = Insert.class)
	private Status status;
	@NotNull(message = "price is not null", groups = {Insert.class, Update.class})
	@PositiveOrZero
	private Integer price;
	@NotBlank(message = "url is not blank", groups = {Insert.class, Update.class})
	@URL(message = "it's not a valid URL")
	private String url;
	@NotBlank(message = "region code is not blank", groups = Update.class)
	private String regionCode;
	private String wishLocationAddress;
	@NotNull(message = "limited number of participants is not null", groups = {Insert.class, Update.class})
	@Positive
	private Integer limitedNumberOfParticipants;
	@NotBlank(message = "description is not blank", groups = {Insert.class, Update.class})
	private String description;
	@NotNull(message = "view count is not null", groups = {Update.class})
	private Integer viewCount;
	private List<ParticipantDTO> participants;
	private List<PhotoDTO> photos;

	@Builder
	public GroupbuyingBoardDTO(Long id, String title, Category category, Status status, Integer price, String url,
		String regionCode, String wishLocationAddress, Integer limitedNumberOfParticipants, String description,
		Integer viewCount, List<ParticipantDTO> participants, List<PhotoDTO> photos) {
		this.id = id;
		this.title = title;
		this.category = category;
		this.status = status;
		this.price = price;
		this.url = url;
		this.regionCode = regionCode;
		this.wishLocationAddress = wishLocationAddress;
		this.limitedNumberOfParticipants = limitedNumberOfParticipants;
		this.description = description;
		this.viewCount = viewCount;
		this.participants = participants;
		this.photos = photos;
	}

	public static GroupbuyingBoardDTO toGroupbuyingBoardDTO(GroupbuyingBoard groupbuyingBoard) {
		return GroupbuyingBoardDTO.builder()
			.id(groupbuyingBoard.getId())
			.title(groupbuyingBoard.getTitle())
			.category(groupbuyingBoard.getCategory())
			.status(groupbuyingBoard.getStatus())
			.price(groupbuyingBoard.getPrice())
			.url(groupbuyingBoard.getUrl())
			.regionCode(groupbuyingBoard.getRegionCode())
			.wishLocationAddress(groupbuyingBoard.getWishLocationAddress())
			.limitedNumberOfParticipants(groupbuyingBoard.getLimitedNumberOfParticipants())
			.description(groupbuyingBoard.getDescription())
			.viewCount(groupbuyingBoard.getViewCount())
			.participants(ParticipantDTO.toParticipantDTOs(groupbuyingBoard.getParticipants()))
			.photos(PhotoDTO.toPhotoDTOs(groupbuyingBoard.getPhotos()))
			.build();
	}

	public static List<GroupbuyingBoardDTO> toGroupbuyingBoardDTOs(List<GroupbuyingBoard> groupbuyingBoards) {
		return groupbuyingBoards.stream()
			.map(GroupbuyingBoardDTO::toGroupbuyingBoardDTO)
			.collect(Collectors.toList());
	}

}
