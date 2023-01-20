package com.gophagi.nanugi.member.service;

import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.member.dto.Profile;
import com.gophagi.nanugi.member.exception.InvaildKakaoTokenExcepion;
import com.gophagi.nanugi.member.exception.NotFoundCodeException;
import com.gophagi.nanugi.member.feignClient.KakaoMember;
import com.gophagi.nanugi.member.dto.KakaoInfo;
import com.gophagi.nanugi.member.dto.KakaoToken;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;


@Slf4j
@Service
@RequiredArgsConstructor //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성
public class KakaoService {

    private final KakaoMember kakaoMember;


    @Value("${kakao.auth-url}")
    private String kakaoAuthUrl;

    @Value("${kakao.user-api-url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.profile-url}")
    private String profileUrl;

    @Value("${kakao.logout-api-url}")
    private String kakaoLogoutApiUrl;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    /**
     * 카카오 코드로 카카오 토큰 받기
     * @param code
     * @return KakaoToken
     */
    public KakaoToken getToken(final String code)  {
        KakaoToken token;
        try {

            token = kakaoMember.getToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code, "authorization_code");
        } catch (Exception ex) {
            if(ex instanceof FeignException.BadRequest){
                throw new NotFoundCodeException(ex, ErrorCode.CODE_NOT_FOUND);
            }
            throw new RuntimeException(ex);
        }
        return token;

    }

    /**
     * 카카오 토큰으로 카카오 사용자 정보 조회
     * @param token
     * @return KakaoInfo
     */
    public KakaoInfo getInfo(final KakaoToken token)  {
        log.info("token = {}", token);
        KakaoInfo kakaoInfo ;

        try {
            Profile profile = kakaoMember.getProfile(new URI(profileUrl), token.getTokenType() + " " + token.getAccessToken());
            log.info("profile :{}", profile);
            kakaoInfo = kakaoMember.getInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken());

            kakaoInfo.getKakaoAccount().getProfile().setProfileImageURL(profile.getProfileImageURL());
            log.info("kakaoInfo :{}", kakaoInfo);

        } catch (Exception ex) {
            if(ex instanceof FeignException.Unauthorized){
                throw new InvaildKakaoTokenExcepion(ex, ErrorCode.INVALID_TOKEN);
            }
            throw new RuntimeException(ex);
        }


        return kakaoInfo;

    }


    public void kakaoLogout(final KakaoToken token) {
        try {
            kakaoMember.kakaoLogout(new URI(kakaoLogoutApiUrl), token.getTokenType()  + " " + token.getAccessToken());
        }
        catch (Exception ex) {
            if(ex instanceof FeignException.Unauthorized){
                throw new InvaildKakaoTokenExcepion(ex, ErrorCode.INVALID_TOKEN);
            }
            throw new RuntimeException(ex);
        }
    }
}
