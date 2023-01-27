package com.gophagi.nanugi.member.controller;

import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.member.dto.KakaoInfo;
import com.gophagi.nanugi.member.dto.KakaoToken;
import com.gophagi.nanugi.member.dto.LoginInfoDTO;
import com.gophagi.nanugi.member.dto.MemberDTO;
import com.gophagi.nanugi.member.exception.MemberNotFoundExcepion;
import com.gophagi.nanugi.member.service.KakaoService;
import com.gophagi.nanugi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final MemberService memberService;


    @GetMapping("/login")
    @ResponseBody
    public LoginInfoDTO getKakaoAccount(@RequestParam("code") String code, HttpServletResponse response) {
        log.info("code = {}", code);

        //코드로 엑세스 토큰 받기
        KakaoToken kakaoToken = kakaoService.getToken(code);
        log.info("kakaoToken = {}", kakaoToken);

        //토큰으로 사용자 정보 조회
        KakaoInfo kakaoInfo = kakaoService.getInfo(kakaoToken);
        log.info("kakaoInfo = {}", kakaoInfo);

        //카카오 사용자 정보로 회원 정보 조회후 로그인
        MemberDTO loginMember = memberService.getMemberByKakao(kakaoInfo);

        if(loginMember != null){
            //카카오 엑세스 토큰 만료
            kakaoService.kakaoLogout(kakaoToken);

            //jwt토큰 생성
            String token = jwtTokenProvider.generateToken(loginMember.getId(),loginMember.getNickname());

            //쿠키에 jwt토큰 저장
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(1000 * 60 * 60 );
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return LoginInfoDTO.toLoginInfoDTO(loginMember,token);


        }else{
            throw new MemberNotFoundExcepion();
        }

    }

    @GetMapping("/logout")
    public String kakaoLogout(HttpServletResponse response){

       //쿠키에서 삭제
        Cookie cookie = new Cookie("token", null);
        //세션 쿠키이므로 웹브라우저 종료시 서버에서 해당 쿠키의 종료 날짜를 0으로 지정
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "ok";
    }


}
