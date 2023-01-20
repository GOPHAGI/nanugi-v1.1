package com.gophagi.nanugi.member.controller;

import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.member.dto.KakaoInfo;
import com.gophagi.nanugi.member.dto.KakaoToken;
import com.gophagi.nanugi.member.dto.LoginInfoDTO;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final MemberService memberService;


    @GetMapping("/login")
    @ResponseBody
    public LoginInfoDTO getKakaoAccount(@RequestParam("code") String code, HttpSession session,Model model) {
        log.info("code = {}", code);

        //코드로 엑세스 토큰 받기
        KakaoToken kakaoToken = kakaoService.getToken(code);
        log.info("kakaoToken = {}", kakaoToken);

        //토큰으로 사용자 정보 조회
        KakaoInfo kakaoInfo = kakaoService.getInfo(kakaoToken);
        log.info("kakaoInfo = {}", kakaoInfo);

        //카카오 사용자 정보로 회원 정보 조회후 로그인
        MemberDTO LoginMember = memberService.getMemberByKakao(kakaoInfo);

        if(LoginMember != null){
            //카카오 엑세스 토큰 만료
            kakaoService.kakaoLogout(kakaoToken);

            //로그인 성공 처리 토큰 발생
            return LoginInfoDTO.builder().userId(LoginMember.getId()).nickname(LoginMember.getNickname())
                    .token(jwtTokenProvider.generateToken(LoginMember.getNickname())).build();

           // session.setAttribute("userId", LoginMember.getId());
           // session.setAttribute("kakaoToken", kakaoToken);
           // model.addAttribute("LoginMember",LoginMember);
        }

        //log.info("session id:{}", session.getId());
        //log.info("session.userId = {}", session.getAttribute("userId"));
        //log.info("session.kakaoToken = {}", session.getAttribute("kakaoToken"));
       // return "login success";
        return null;
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
