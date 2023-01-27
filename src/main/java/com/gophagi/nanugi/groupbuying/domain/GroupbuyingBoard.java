package com.gophagi.nanugi.groupbuying.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.URL;

import com.gophagi.nanugi.common.util.file.domain.Photo;
import com.gophagi.nanugi.common.util.timestamp.BaseTime;
import com.gophagi.nanugi.groupbuying.constant.Category;
import com.gophagi.nanugi.groupbuying.constant.Status;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class GroupbuyingBoard extends BaseTime {
	@Id
	@Column(name = "GROUPBUYING_BOARD_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	@Enumerated(value = EnumType.STRING)
	private Category category;
	@Enumerated(value = EnumType.STRING)
	private Status status;
	@PositiveOrZero
	private Integer price;
	@URL(message = "It's not a valid url")
	private String url;
	private String regionCode;
	private String wishLocationAddress;
	private Integer limitedNumberOfParticipants;
	private String description;
	private Integer viewCount;
	@OneToMany(mappedBy = "groupbuyingBoard", orphanRemoval = true)
	private List<Participant> participants;
	@Valid
	@OneToMany(mappedBy = "groupbuyingBoard", orphanRemoval = true)
	private List<Photo> photos;

	@Builder
	public GroupbuyingBoard(Long id, String title, Category category, Status status, Integer price,
		String url, String regionCode, String wishLocationAddress,
		Integer limitedNumberOfParticipants, String description,
		Integer viewCount, List<Participant> participants, List<Photo> photos) {
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

	@PrePersist
	public void prePersist() {
		this.status = Status.GATHERING;
		this.viewCount = 0;
	}

	public static GroupbuyingBoard toGroupbuyingBoard(GroupbuyingBoardDTO dto) {
		return GroupbuyingBoard.builder()
			.id(dto.getId())
			.title(dto.getTitle())
			.category(dto.getCategory())
			.status(dto.getStatus())
			.price(dto.getPrice())
			.url(dto.getUrl())
			.regionCode(dto.getRegionCode())
			.wishLocationAddress(dto.getWishLocationAddress())
			.limitedNumberOfParticipants(dto.getLimitedNumberOfParticipants())
			.description(dto.getDescription())
			.viewCount(dto.getViewCount())
			.build();
	}

	public void update(GroupbuyingBoardDTO dto) {
		this.title = dto.getTitle();
		this.category = dto.getCategory();
		this.status = dto.getStatus();
		this.price = dto.getPrice();
		this.url = dto.getUrl();
		this.regionCode = dto.getRegionCode();
		this.wishLocationAddress = dto.getWishLocationAddress();
		this.limitedNumberOfParticipants = dto.getLimitedNumberOfParticipants();
		this.description = dto.getDescription();
		this.viewCount = dto.getViewCount();
	}

	public boolean isFull() {
		return this.getParticipants().size() == this.getLimitedNumberOfParticipants();
	}

	public boolean participateInDuplicate(Long userId) {
		for (Participant participant : participants) {
			if (participant.getMember().getId().equals(userId)) {
				return true;
			}
		}
		return false;
	}

	public void deletePhoto(List<Long> deletePhotoIdList) {
		if (Objects.isNull(deletePhotoIdList)) {
			return;
		}

		Map<Long, Photo> test = new HashMap<>();
		for (int i = 0; i < photos.size(); i++) {
			test.put(photos.get(i).getFileId(), photos.get(i));
		}

		for (Long deletePhotoId : deletePhotoIdList) {
			if (test.containsKey(deletePhotoId)) {
				photos.remove(test.get(deletePhotoId));
			}
		}
	}
}
