package com.growith.tailo.search.feed.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record FeedSearchResponse(
        Long feedPostId,
        String accountId,
        String nickname,
        String profileImageUrl,
        String content,
        List<String> hashtags,
        List<String> imageUrls,
        String createdAt,
        String updatedAt,
        boolean isFollowing,
        Long likesCount,
        Long commentsCount,
        boolean isLiked
) {
}

