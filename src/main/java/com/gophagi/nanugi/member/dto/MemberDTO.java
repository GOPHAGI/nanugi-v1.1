package com.gophagi.nanugi.member.dto;

import com.gophagi.nanugi.member.domain.Member;
import com.gophagi.nanugi.member.domain.Role;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberDTO {

    private Long id;
    private Long kakaoid;
    private String nickname;
    private String email;
    private Role role;
    private String activated;
    private BigDecimal ratingScore;

    public static MemberDTO toMemberDTO(Member member){
        return MemberDTO.builder()
                .id(member.getId())
                .kakaoid(member.getKakaoid())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .role(member.getRole())
                .activated(member.getActivated())
                .ratingScore(member.getRatingScore())
                .build();
    }
}
