package com.growith.tailo.member.dto.response;

public record MemberListResponse(String nickname,
                                 String accountId,
                                 String profileImageUrl,
                                 boolean isFollowing) {
}
