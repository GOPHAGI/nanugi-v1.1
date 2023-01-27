package com.gophagi.nanugi.groupbuying.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import com.gophagi.nanugi.common.util.area.Areas;
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
	private Areas deliveryArea;
	@NotBlank(message = "delivery address is not blank", groups = {Insert.class, Update.class})
	private String deliveryAddress;
	@NotBlank(message = "delivery detail address is not blank", groups = {Insert.class, Update.class})
	private String deliveryDetailAddress;
	private LocalDateTime expirationDate;
	@NotNull(message = "limited number of participants is not null", groups = {Insert.class, Update.class})
	private Integer limitedNumberOfParticipants;
	@NotBlank(message = "description is not blank", groups = {Insert.class, Update.class})
	private String description;
	private Integer viewCount;
	private List<PhotoDTO> photos;

	@Builder
	public GroupbuyingBoardDTO(Long id, String title, Category category, Status status, Integer price, String url,
		Areas deliveryArea, String deliveryAddress, String deliveryDetailAddress, LocalDateTime expirationDate,
		Integer limitedNumberOfParticipants, String description, Integer viewCount, List<PhotoDTO> photos) {
		this.id = id;
		this.title = title;
		this.category = category;
		this.status = status;
		this.price = price;
		this.url = url;
		this.deliveryArea = deliveryArea;
		this.deliveryAddress = deliveryAddress;
		this.deliveryDetailAddress = deliveryDetailAddress;
		this.expirationDate = expirationDate;
		this.limitedNumberOfParticipants = limitedNumberOfParticipants;
		this.description = description;
		this.viewCount = viewCount;
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
			.deliveryAddress(groupbuyingBoard.getDeliveryAddress())
			.deliveryDetailAddress(groupbuyingBoard.getDeliveryDetailAddress())
			.expirationDate(groupbuyingBoard.getExpirationDate())
			.limitedNumberOfParticipants(groupbuyingBoard.getLimitedNumberOfParticipants())
			.description(groupbuyingBoard.getDescription())
			.viewCount(groupbuyingBoard.getViewCount())
			.photos(PhotoDTO.toPhotoDTOs(groupbuyingBoard.getPhotos()))
			.build();
	}

	public static List<GroupbuyingBoardDTO> toGroupbuyingBoardDTOs(List<GroupbuyingBoard> groupbuyingBoards) {
		return groupbuyingBoards.stream()
			.map(GroupbuyingBoardDTO::toGroupbuyingBoardDTO)
			.collect(Collectors.toList());
	}

}
