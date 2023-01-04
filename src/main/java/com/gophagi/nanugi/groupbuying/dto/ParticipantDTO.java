package com.gophagi.nanugi.groupbuying.dto;

import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.domain.GroupbuyingBoard;
import com.gophagi.nanugi.groupbuying.domain.Participant;
import com.gophagi.nanugi.member.domain.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ParticipantDTO {

	private Long id;
	private Member member;
	private GroupbuyingBoard groupbuyingBoard;
	private Role role;

	@Builder
	public ParticipantDTO(Long id, Member member, GroupbuyingBoard groupbuyingBoard, Role role) {
		this.id = id;
		this.member = member;
		this.groupbuyingBoard = groupbuyingBoard;
		this.role = role;
	}

	public static ParticipantDTO toParticipantDTO(Participant participant) {
		return ParticipantDTO.builder()
			.id(participant.getId())
			.member(participant.getMember())
			.groupbuyingBoard(participant.getGroupbuyingBoard())
			.role(participant.getRole())
			.build();
	}
}
