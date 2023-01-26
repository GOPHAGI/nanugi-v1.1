package com.gophagi.nanugi.groupbuying.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gophagi.nanugi.groupbuying.constant.Role;
import com.gophagi.nanugi.groupbuying.domain.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
	Optional<List<Participant>> findByMemberId(Long id);

	Optional<List<Participant>> findByGroupbuyingBoardId(Long id);

	Optional<List<Participant>> findByMemberIdAndRole(Long userId, Role role);

	Optional<Participant> findByGroupbuyingBoardIdAndMemberId(Long boardId, Long userId);
}
