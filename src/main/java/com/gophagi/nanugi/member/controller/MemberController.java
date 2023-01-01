package com.gophagi.nanugi.member.controller;

import com.gophagi.nanugi.member.dto.MemberDTO;
import com.gophagi.nanugi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    @RequestMapping
    public MemberDTO memberView(HttpSession session, Model model){

        Long userId = (Long) session.getAttribute("userId");
        MemberDTO member = memberService.getMemberById(userId);
        model.addAttribute("member",member);

        return member;
    }
    @PostMapping("/update")
    public MemberDTO updateMember(@ModelAttribute MemberDTO updatedMember,
                               @RequestParam Long userId){

        return memberService.updateMember(userId, updatedMember);
    }

    @PostMapping("/my")
    public String myPageView(@RequestParam Long userId, Model model){

        //멤버 정보 가져오기
        MemberDTO member = memberService.getMemberById(userId);
        model.addAttribute("member",member);

        //todo: 공구진행 목록 전체 가져오기

        return  "ok";
    }

    //마이페이지 - 공동구매 내역조회(구매)
    @PostMapping("/my/order")
    public String getGroupbuyingOrderList(@RequestParam Long userId, Model model){

        //todo: 공구 구매 목록 전체 가져오기

        return  "ok";
    }

    //마이페이지 - 공동구매 내역조회(판매)
    @PostMapping("/my/selling")
    public String getGroupbuyingSellingList(@RequestParam Long userId, Model model){

        //todo: 공구 구매 목록 전체 가져오기

        return  "ok";
    }
}
