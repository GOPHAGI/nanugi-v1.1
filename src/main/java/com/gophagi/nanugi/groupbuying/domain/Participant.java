package com.gophagi.nanugi.groupbuying.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.gophagi.nanugi.common.util.timestamp.BaseTime;
import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.dto.ParticipantDTO;
import com.gophagi.nanugi.member.domain.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Participant extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Member member;
	@ManyToOne
	private GroupbuyingBoard groupbuyingBoard;
	@Enumerated(value = EnumType.STRING)
	private Role role;

	@Builder
	public Participant(Long id, Member member, GroupbuyingBoard groupbuyingBoard, Role role) {
		this.id = id;
		this.member = member;
		this.groupbuyingBoard = groupbuyingBoard;
		this.role = role;
	}

	public static Participant toParticipant(ParticipantDTO dto) {
		return Participant.builder()
			.member(dto.getMember())
			.groupbuyingBoard(dto.getGroupbuyingBoard())
			.role(dto.getRole())
			.build();
	}
}
