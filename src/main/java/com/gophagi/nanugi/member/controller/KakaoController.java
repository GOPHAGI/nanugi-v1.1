package com.gophagi.nanugi.member.controller;

import com.gophagi.nanugi.member.dto.KakaoInfo;
import com.gophagi.nanugi.member.dto.KakaoToken;
import com.gophagi.nanugi.member.dto.MemberDTO;
import com.gophagi.nanugi.member.service.KakaoService;
import com.gophagi.nanugi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final MemberService memberService;


    @GetMapping("/login")
    public String getKakaoAccount(@RequestParam("code") String code, HttpSession session,Model model) {
        log.info("code = {}", code);

        //코드로 토큰 받기
        KakaoToken kakaoToken = kakaoService.getToken(code);

        //토큰으로 사용자 정보 조회
        KakaoInfo kakaoInfo = kakaoService.getInfo(kakaoToken);
        log.info("kakaoInfo = {}", kakaoInfo);

        //카카오 사용자 정보로 회원 정보 조회후 로그인
        MemberDTO LoginMember = memberService.getMemberByKakao(kakaoInfo);

        if(LoginMember != null){

            //로그인 성공 처리
            session.setAttribute("userId", LoginMember.getId());
            session.setAttribute("kakaoToken", kakaoToken);
            model.addAttribute("LoginMember",LoginMember);
        }

        log.info("session id:{}", session.getId());
        log.info("session.userId = {}", session.getAttribute("userId"));
        log.info("session.kakaoToken = {}", session.getAttribute("kakaoToken"));
        return "login success";
    }

    @GetMapping("/logout")
    public String kakaoLogout(HttpSession session){

        //세션 로그아웃
        kakaoService.kakaoLogout((KakaoToken)session.getAttribute("kakaoToken"));
        session.removeAttribute("userId");
        session.removeAttribute("kakaoToken");

        log.info("session.userId = {}", session.getAttribute("userId"));
        log.info("session.kakaoToken = {}", session.getAttribute("kakaoToken"));

        return "logout success";

    }
}
