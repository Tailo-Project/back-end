package com.growith.tailo.feed.likes.dto.response;

import java.util.List;

public record LikedFeedIdsResponse(
        List<Long> likedFeedIds
) {
}
