package com.growith.tailo.block.repository;


import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<BlockMember, Long>,BlockQueryDSLRepository {
    boolean existsByBlockerAndBlocked(Member member, Member blockedMember);
}
