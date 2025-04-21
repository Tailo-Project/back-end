package com.growith.tailo.block.repository;

import com.growith.tailo.block.dto.response.BlockResponse;
import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlockCustomRepository {

    Page<BlockResponse> findAllBlockedMember(Member member, Pageable pageable);
}
