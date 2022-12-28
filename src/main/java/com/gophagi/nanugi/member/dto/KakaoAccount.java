package com.gophagi.nanugi.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoAccount {
    private Profile profile;
    private String email;
}
