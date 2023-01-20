package com.gophagi.nanugi.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
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

}
