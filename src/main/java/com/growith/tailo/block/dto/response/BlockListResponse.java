package com.growith.tailo.block.dto.response;

import com.growith.tailo.block.entity.BlockMember;
import lombok.Builder;

@Builder
public record BlockListResponse(Long blockedId, String accountId, String nickname, String profileImageUrl){
    public static BlockListResponse fromBlockMember(BlockMember blockMember){
        return BlockListResponse
                .builder()
                .blockedId(blockMember.getBlocked().getId())
                .accountId(blockMember.getBlocked().getAccountId())
                .nickname(blockMember.getBlocked().getNickname())
                .profileImageUrl(blockMember.getBlocked().getProfileImageUrl())
                .build();
    }
}

