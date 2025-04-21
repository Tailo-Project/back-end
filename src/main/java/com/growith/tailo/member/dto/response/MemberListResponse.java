package com.growith.tailo.member.dto.response;

public record MemberListResponse(Long id,
                                 String nickname,
                                 String accountId,
                                 String profileImageUrl,
                                 boolean isFollow) {
}
