package com.gophagi.nanugi.member.dto;

import com.gophagi.nanugi.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginInfoDTO {
    private Long userId;
    private String nickname;
    private String token;

    @Builder
    public LoginInfoDTO(Long userId, String nickname, String token) {
        this.userId = userId;
        this.nickname = nickname;
        this.token = token;
    }

    public static LoginInfoDTO toLoginInfoDTO(MemberDTO member,String token){
        return LoginInfoDTO.builder()
                .userId(member.getId())
                .nickname(member.getNickname())
                .token(token)
                .build();
    }
}
