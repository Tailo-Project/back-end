package com.growith.tailo.search.feed.dto;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record FeedSearchResponseList(
        List<FeedSearchResponse> feedSearchResponses,
        Pagination pagination
) {
}
