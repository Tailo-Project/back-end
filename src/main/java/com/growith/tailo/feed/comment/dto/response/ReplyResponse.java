package com.growith.tailo.feed.comment.dto.response;

import java.time.LocalDateTime;

public record ReplyResponse(
        Long commentId,
        String content,
        String accountId,
        String authorNickname,
        String authorProfile,
        LocalDateTime createdAt
) {
}
