package com.growith.tailo.member.dto.response;

public record MemberResponse(String nickname,
                             String accountId,
                             Integer countFollower,
                             Integer countFollowing,
                             String profileImageUrl) {
}
