package com.growith.tailo.block.repository;

import com.growith.tailo.block.dto.response.BlockListResponse;
import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.member.entity.Member;

import java.util.List;

public interface BlockQueryDSLRepository {

    List<BlockMember> findAllBlockedMember(Member member);
}
