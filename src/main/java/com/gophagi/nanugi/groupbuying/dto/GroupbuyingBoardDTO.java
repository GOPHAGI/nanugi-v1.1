package com.gophagi.nanugi.groupbuying.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.gophagi.nanugi.common.util.file.dto.PhotoDTO;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.constant.Status;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.vo.ParticipantVO;

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
	@NotBlank(message = "id is not blank")
	private Long id;
	private String title;
	private Category category;
	private Status status;
	private Integer price;
	private String url;
	private String regionCode;
	private String wishLocationAddress;
	private Integer limitedNumberOfParticipants;
	private String description;
	private Integer viewCount;
	private List<ParticipantVO> participants;
	private List<PhotoDTO> photos;

	@Builder
	public GroupbuyingBoardDTO(Long id, String title, Category category, Status status, Integer price, String url,
		String regionCode, String wishLocationAddress, Integer limitedNumberOfParticipants, String description,
		Integer viewCount, List<ParticipantVO> participants, List<PhotoDTO> photos) {
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
			.participants(ParticipantVO.toParticipantVOs(groupbuyingBoard.getParticipants()))
			.photos(PhotoDTO.toPhotoDTOs(groupbuyingBoard.getPhotos()))
			.build();
	}

	public static List<GroupbuyingBoardDTO> toGroupbuyingBoardDTOs(List<GroupbuyingBoard> groupbuyingBoards) {
		return groupbuyingBoards.stream()
			.map(GroupbuyingBoardDTO::toGroupbuyingBoardDTO)
			.collect(Collectors.toList());
	}

}
