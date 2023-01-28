package com.gophagi.nanugi.groupbuying.vo;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.domain.Participant;
import com.gophagi.nanugi.member.dto.MemberDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ParticipantVO {

	private Long id;
	@Valid
	private MemberDTO member;
	private Role role;

	@Builder
	public ParticipantVO(Long id, MemberDTO member, Role role) {
		this.id = id;
		this.member = member;
		this.role = role;
	}

	public static ParticipantVO toParticipantVO(Participant participant) {
		return ParticipantVO.builder()
			.id(participant.getId())
			.member(MemberDTO.toMemberDTO(participant.getMember()))
			.role(participant.getRole())
			.build();
	}

	public static List<ParticipantVO> toParticipantVOs(List<Participant> participants) {
		return participants.stream()
			.map(ParticipantVO::toParticipantVO)
			.collect(Collectors.toList());
	}
}
