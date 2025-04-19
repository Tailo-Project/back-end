package com.growith.tailo.feed.feed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FeedPostResponse(
        Long feedId,
        String content,
        String authorNickname,
        String authorProfile,
        List<String> imageUrls,
        List<String> hashtags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long likesCount,
        long commentsCount
) {
}
