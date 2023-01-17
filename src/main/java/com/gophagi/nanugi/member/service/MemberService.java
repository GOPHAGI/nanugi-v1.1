package com.gophagi.nanugi.member.service;

import com.gophagi.nanugi.common.excepion.ErrorCode;
import com.gophagi.nanugi.member.domain.Member;
import com.gophagi.nanugi.member.dto.KakaoInfo;
import com.gophagi.nanugi.member.dto.MemberDTO;
import com.gophagi.nanugi.member.exception.DuplicateMemberExcepion;
import com.gophagi.nanugi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    /**
     * 카카오아이디로 멤버 찾기
     * 저장 안되어있으면 저장 후 리턴
     * @param kakaoInfo
     * @return Member
     */
    public MemberDTO getMemberByKakao (KakaoInfo kakaoInfo) {
        //정보 저장 되어있는지 확인

        Long kakaoid = kakaoInfo.getId();
        log.info("kakaoid = {}", kakaoid);
        Member member = memberRepository.findByKakaoid(kakaoid);
        log.info("member = {}", member);

        //저장 안되어있으면 저장
        if(member == null){
            return saveMember(kakaoInfo, kakaoid);
        }
        ///저장 되어잇으면 저장 되어잇는것 리턴
        return MemberDTO.toMemberDTO(member);
    }

    /**
     * 회원을 아이디로 찾기
     * @param userId
     * @return Member
     */
    public MemberDTO getMemberById(Long userId) {

        MemberDTO member = MemberDTO.toMemberDTO(memberRepository.findByid(userId));
        log.info("member = {}", member);

        return member;
    }

    /**
     * 회원 저장
     * @param kakaoInfo
     * @param kakaoid
     */
    private MemberDTO saveMember(KakaoInfo kakaoInfo, Long kakaoid) {

        Member member = memberRepository.findByKakaoid(kakaoid);
        if(member != null){
            //이미 저장 되어 있을 경우 excepion발생
            throw new DuplicateMemberExcepion();
        }

        MemberDTO newMember = new MemberDTO();
        newMember.setKakaoid(kakaoid);
        newMember.setNickname(kakaoInfo.getKakaoAccount().getProfile().getNickname());
        newMember.setEmail(kakaoInfo.getKakaoAccount().getEmail());
        newMember.setProfileImageURL(kakaoInfo.getKakaoAccount().getProfile().getProfileImageURL());

        return MemberDTO.toMemberDTO(memberRepository.save(Member.toMember(newMember)));
    }

    /**
     * 회원 정보 업데이트
     * @param userId
     * @param member
     * @return MemberDTO
     */
    public MemberDTO updateMember(Long userId, MemberDTO member) {

        MemberDTO findMember = MemberDTO.toMemberDTO(memberRepository.findByid(userId));
        findMember.setNickname(member.getNickname());
        findMember.setActivated(member.getActivated());
        findMember.setRatingScore(member.getRatingScore());

        memberRepository.save(Member.toMember(findMember));

        return findMember;
    }




}
