package com.gophagi.nanugi.member.dto;

import com.gophagi.nanugi.member.domain.Member;
import com.gophagi.nanugi.member.domain.Role;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {

    private Long id;
    private Long kakaoid;
    private String nickname;
    private String email;
    private Role role;
    private String activated;
    private BigDecimal ratingScore;

    @Builder
    public MemberDTO(Long id, Long kakaoid, String nickname, String email, Role role, String activated, BigDecimal ratingScore) {
        this.id = id;
        this.kakaoid = kakaoid;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.activated = activated;
        this.ratingScore = ratingScore;
    }

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
