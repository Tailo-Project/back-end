package com.growith.tailo.block.repository;


import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<BlockMember, Long>, BlockCustomRepository {

    boolean existsByBlockerAndBlocked(Member member, Member blockedMember);

    Optional<BlockMember> findByBlockerAndBlocked(Member member, Member blockedMember);

    double countBlockMemberByBlocker(Member blocker);
}
