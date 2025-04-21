package com.growith.tailo.feed.feedImage.dto.response;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record MemberFeedImageListResponse(
        List<MemberFeedImageResponse> memberFeedImages,
        Pagination pagination
) {
}
