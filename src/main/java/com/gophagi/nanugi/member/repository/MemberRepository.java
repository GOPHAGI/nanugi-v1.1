package com.gophagi.nanugi.member.repository;

import com.gophagi.nanugi.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member,Long> {

    Member save(Member member);

    //정보확인
    Member findByKakaoid(Long kakaoid);

    Member findByid(Long userId);
}
