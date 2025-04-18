package com.growith.tailo.block.repository;

import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.member.entity.Member;

import java.util.List;

public interface BlockQueryDSLRepository {

    List<BlockMember> findAllBlockMember(Member member);
}
