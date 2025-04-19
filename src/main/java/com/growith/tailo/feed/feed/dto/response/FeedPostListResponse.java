package com.growith.tailo.feed.feed.dto.response;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record FeedPostListResponse(
        List<FeedPostResponse> feedPosts,
        Pagination pagination
) {
}
