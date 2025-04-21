package com.growith.tailo.follow.dto.response;

public record FollowMeResponse(Long id,
                               String nickname,
                               String accountId,
                               String profileImageUrl,
                               boolean isFollow) {
}
