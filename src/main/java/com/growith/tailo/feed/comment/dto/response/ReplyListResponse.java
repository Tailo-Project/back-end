package com.growith.tailo.feed.comment.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReplyListResponse(
        List<ReplyResponse> replies,
        int totalCount
) {
}
