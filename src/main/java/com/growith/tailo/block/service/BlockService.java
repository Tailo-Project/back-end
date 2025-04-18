package com.growith.tailo.block.service;

import com.growith.tailo.block.dto.response.BlockListResponse;
import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.block.repository.BlockRepository;
import com.growith.tailo.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;

    // 차단된 사용자 목록을 반환하는 메서드
    public List<BlockListResponse> getList(Member member) {
        // BlockRepository의 findAllBlockMember 메서드를 호출하여 차단된 사용자 목록을 가져옴
        List<BlockMember> blockList = blockRepository.findAllBlockMember(member);

        // 차단된 사용자 목록이 없으면 빈 리스트 반환
        if (blockList == null || blockList.isEmpty()) {
            return List.of();
        }

        // BlockMember 엔티티를 BlockListResponse DTO로 변환하여 반환
        return blockList.stream()
                .map(BlockListResponse::fromBlockMember)
                .collect(Collectors.toList());
    }
}
