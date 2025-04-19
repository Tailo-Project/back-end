package com.growith.tailo.block.service;

import com.growith.tailo.block.dto.response.BlockListResponse;
import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.block.repository.BlockRepository;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.exception.ResourceAlreadyExistException;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;

    // 차단된 사용자 목록을 반환하는 메서드
    public List<BlockListResponse> getListService(Member member) {
        // BlockRepository의 findAllBlockMember 메서드를 호출하여 차단된 사용자 목록을 가져옴
        List<BlockMember> blockList = blockRepository.findAllBlockedMember(member);

        // 차단된 사용자 목록이 없으면 빈 리스트 반환
        if (blockList.isEmpty()) {
            return List.of();
        }

        // BlockMember 엔티티를 BlockListResponse DTO로 변환하여 반환
        return blockList.stream()
                .map(BlockListResponse::fromBlockMember)
                .collect(Collectors.toList());
    }
    // 사용자 차단
    public String blockingMemberService(Member member, String accountId){
        if (member.getAccountId().equals(accountId)) {
            throw new IllegalArgumentException("자기 자신을 차단할 수 없습니다.");
        }
        Member blockedMember = memberRepository.findByAccountId(accountId).orElseThrow(
                ()->new ResourceNotFoundException("사용자를 찾을 수 없습니다.")
        );
        boolean alreadyBlocked = blockRepository.existsByBlockerAndBlocked(member, blockedMember);
        if (alreadyBlocked) {
            throw new ResourceAlreadyExistException("이미 차단한 사용자입니다.");
        }
        BlockMember block = BlockMember.builder()
                .blocker(member).blocked(blockedMember).build();
        blockRepository.save(block);
        return "사용자를 차단했습니다.";
    }
    // 사용자 차단 해제
    public String deleteBlockedService(Member member, String accountId){
        Member blockedMember = memberRepository.findByAccountId(accountId).orElseThrow(
                ()-> new ResourceNotFoundException("사용자를 찾을 수 없습니다.")
        );
        BlockMember blockTable = blockRepository.findByBlockerAndBlocked(member,blockedMember).orElseThrow(
                ()->new ResourceNotFoundException("차단한 사용자를 찾을 수 없습니다.")
        );
        blockRepository.delete(blockTable);
        return "차단 해제 완료";
    }
}
