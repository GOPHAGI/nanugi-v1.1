package com.gophagi.nanugi.member.controller;

import com.gophagi.nanugi.common.jwt.JwtTokenProvider;
import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import com.gophagi.nanugi.groupbuying.service.GroupbuyingBoardQueryService;
import com.gophagi.nanugi.member.dto.MemberDTO;
import com.gophagi.nanugi.member.dto.MemberGroupbuyingBoardDTO;
import com.gophagi.nanugi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final GroupbuyingBoardQueryService groupbuyingBoardQueryService;
    @RequestMapping
    public MemberDTO memberInfo(@CookieValue String token){
        Long userId = JwtTokenProvider.getUserIdFromJwt(token);
        MemberDTO member = memberService.getMemberById(userId);

        return member;
    }
    @PostMapping("/update")
    public MemberDTO updateMember(@ModelAttribute MemberDTO updatedMember,
                                  @CookieValue String token){
        Long userId = JwtTokenProvider.getUserIdFromJwt(token);
        return memberService.updateMember(userId, updatedMember);
    }

    @PostMapping("/my")
    public MemberGroupbuyingBoardDTO myPageView(@CookieValue String token){
        Long userId = JwtTokenProvider.getUserIdFromJwt(token);
        //멤버 정보 가져오기
        MemberDTO member = memberService.getMemberById(userId);
        //model.addAttribute("member",member);

        //공구진행 목록 전체 가져오기
        List<GroupbuyingBoardDTO> groupbuyingBoardDTOList =  groupbuyingBoardQueryService.searchGroupbuyingBoardByUserId(userId);
        MemberGroupbuyingBoardDTO memberGroupbuyingBoardDTO = new MemberGroupbuyingBoardDTO(member, groupbuyingBoardDTOList);
        return  memberGroupbuyingBoardDTO;
    }

    //마이페이지 - 공동구매 내역조회(구매)
    @PostMapping("/my/order")
    public List<GroupbuyingBoardDTO> getGroupbuyingOrderList(@CookieValue String token){
        Long userId = JwtTokenProvider.getUserIdFromJwt(token);
        //todo: 공구 구매 목록 전체 가져오기 role= PARTICIPANT
        List<GroupbuyingBoardDTO> groupbuyingBoardDTOList =  groupbuyingBoardQueryService.searchGroupbuyingBoardByUserId(userId);

        return  groupbuyingBoardDTOList;
    }

    //마이페이지 - 공동구매 내역조회(판매)
    @PostMapping("/my/selling")
    public List<GroupbuyingBoardDTO> getGroupbuyingSellingList(@CookieValue String token){
        Long userId = JwtTokenProvider.getUserIdFromJwt(token);
        //todo: 공구 판매 목록 전체 가져오기 participant role =PROMOTER
        List<GroupbuyingBoardDTO> groupbuyingBoardDTOList =  groupbuyingBoardQueryService.searchGroupbuyingBoardByUserId(userId);

        return  groupbuyingBoardDTOList;
    }
}
