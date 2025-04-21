package com.growith.tailo.block.dto.response;

import com.growith.tailo.block.entity.BlockMember;
import lombok.Builder;

@Builder
public record BlockResponse(Long blockedId, String accountId, String nickname, String profileImageUrl){
    public static BlockResponse fromBlockMember(BlockMember blockMember){
        return BlockResponse
                .builder()
                .blockedId(blockMember.getBlocked().getId())
                .accountId(blockMember.getBlocked().getAccountId())
                .nickname(blockMember.getBlocked().getNickname())
                .profileImageUrl(blockMember.getBlocked().getProfileImageUrl())
                .build();
    }
}

