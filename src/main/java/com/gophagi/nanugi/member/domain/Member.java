package com.gophagi.nanugi.member.domain;

import com.gophagi.nanugi.common.util.timestamp.BaseTime;
import com.gophagi.nanugi.member.dto.MemberDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    //@Column(nullable = false) // column의 조건
    private Long kakaoid;

    @Column(length = 20)
    private String nickname;

    @Column(length = 20)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    @Column(length = 20)
    private String activated;

    @Column(precision = 3, scale = 1)
    private BigDecimal ratingScore;

    @Column
    private String profileImageURL;

    public Member(MemberBuilder memberBuilder) {
        super();
    }

    /**
     * insert 되기전 (persist 되기전) 실행된다.
     * default 값 설정
     * */
    @PrePersist
    public void prePersist() {
        this.role = this.role == null ? Role.BASIC : this.role;
        this.activated = this.activated == null ? "Y" : this.activated;
        this.ratingScore = this.ratingScore == null ? new BigDecimal("0") : this.ratingScore;
    }

    @Builder
    public Member(Long id, Long kakaoid, String nickname,
                  String email, Role role, String activated,
                  BigDecimal ratingScore, String profileImageURL) {
        this.id = id;
        this.kakaoid = kakaoid;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.activated = activated;
        this.ratingScore = ratingScore;
        this.profileImageURL = profileImageURL;
    }

    public static Member toMember(MemberDTO member){
        return Member.builder()
                .id(member.getId())
                .kakaoid(member.getKakaoid())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .role(member.getRole())
                .activated(member.getActivated())
                .ratingScore(member.getRatingScore())
                .profileImageURL(member.getProfileImageURL())
                .build();
    }
}
