package com.growith.tailo.feed.comment.dto.response;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record ReplyTotalListResponse(
        List<ReplyResponse> replies,
        Pagination pagination
) {
}
