package com.gophagi.nanugi.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gophagi.nanugi.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor //파라미터가 없는 기본 생성자를 생성
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class) //json 응답 파라미터에서 snake_case 네이밍 규칙 적용
public class KakaoInfo {
    private Long id;
    private KakaoAccount kakaoAccount;

    public static KakaoInfo fail(){
        return null;
    }

}
