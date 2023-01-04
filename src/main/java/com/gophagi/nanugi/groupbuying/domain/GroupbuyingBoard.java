package com.gophagi.nanugi.groupbuying.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

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
	private Integer price;
	private String url;
	private String deliveryAddress;
	private String deliveryDetailAddress;
	private LocalDateTime expirationDate;
	private Integer limitedNumberOfParticipants;
	private String description;
	private Integer viewCount;
	@OneToMany(mappedBy = "groupbuyingBoard")
	private List<Participant> participants;

	@OneToMany(mappedBy = "groupbuyingBoard")
	private List<Photo> photos;

	@Builder
	public GroupbuyingBoard(Long id, String title, Category category, Status status, Integer price,
		String url, String deliveryAddress, String deliveryDetailAddress,
		LocalDateTime expirationDate, Integer limitedNumberOfParticipants, String description,
		Integer viewCount, List<Participant> participants, List<Photo> photos) {
		this.id = id;
		this.title = title;
		this.category = category;
		this.status = status;
		this.price = price;
		this.url = url;
		this.deliveryAddress = deliveryAddress;
		this.deliveryDetailAddress = deliveryDetailAddress;
		this.expirationDate = expirationDate;
		this.limitedNumberOfParticipants = limitedNumberOfParticipants;
		this.description = description;
		this.viewCount = viewCount;
		this.participants = participants;
		this.photos = photos;
	}

	@PrePersist
	public void prePersist() {
		this.status = Status.ONGOING;
		this.expirationDate = LocalDateTime.now().plusDays(7);
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
			.deliveryAddress(dto.getDeliveryAddress())
			.deliveryDetailAddress(dto.getDeliveryDetailAddress())
			.expirationDate(dto.getExpirationDate())
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
		this.deliveryAddress = dto.getDeliveryAddress();
		this.deliveryDetailAddress = dto.getDeliveryDetailAddress();
		this.expirationDate = dto.getExpirationDate();
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
}
