package com.gophagi.nanugi.groupbuying.dto;

import java.time.LocalDateTime;

import com.gophagi.nanugi.common.util.area.Areas;
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
public class GroupbuyingBoardDTO {
	private Long id;
	private String title;
	private Category category;
	private Status status;
	private Integer price;
	private String url;
	private Areas deliveryArea;
	private String deliveryAddress;
	private String deliveryDetailAddress;
	private LocalDateTime expirationDate;
	private Integer limitedNumberOfParticipants;
	private String description;
	private Integer viewCount;

	@Builder
	public GroupbuyingBoardDTO(Long id, String title, Category category, Status status, Integer price, String url,
		Areas deliveryArea, String deliveryAddress, String deliveryDetailAddress, LocalDateTime expirationDate,
		Integer limitedNumberOfParticipants, String description, Integer viewCount) {
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
			.build();
	}
}
