package com.growith.tailo.feed.feedImage.dto.response;

import java.time.LocalDateTime;

public record MemberFeedImageResponse(
        Long feedId,
        String imageUrl,
        LocalDateTime createdAt
) {
}
