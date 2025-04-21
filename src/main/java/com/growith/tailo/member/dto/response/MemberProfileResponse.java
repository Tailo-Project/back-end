package com.growith.tailo.member.dto.response;

import lombok.Builder;


@Builder(toBuilder = true) //이미 생성된 객체의 불변성을 유지하면서 원하는 필드를 동적으로 관리
public record MemberProfileResponse(String nickname,
                                    String accountId,
                                    Long countFollower,
                                    Long countFollowing,
                                    String profileImageUrl,
                                    Boolean isFollow) {


}
