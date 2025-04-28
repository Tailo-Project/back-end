package com.growith.tailo.search.member.dto;

import lombok.Builder;

@Builder
public record MemberSearchResponse(
        Long id,
        String accountId,
        String nickname,
        String profileImageUrl,
        String createdAt,
        String updatedAt,
        boolean isFollowing
) {
}
