package com.growith.tailo.member.mapper.from;

import com.growith.tailo.member.dto.response.AuthResponse;
import com.growith.tailo.member.dto.response.MemberDetailResponse;
import com.growith.tailo.member.dto.response.MemberProfileResponse;
import com.growith.tailo.member.entity.Member;

public class FromMemberMapper {
    public static AuthResponse fromMemberLogin(String email, String accountId, String accessToken){
        return AuthResponse
                .builder()
                .email(email)
                .accountId(accountId)
                .accessToken(accessToken)
                .build();
    }

    public static MemberDetailResponse fromMemberDetail(Member member){
        return MemberDetailResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .accountId(member.getAccountId())
                .breed(member.getBreed())
                .type(member.getType())
                .gender(member.getGender())
                .age(member.getAge())
                .address(member.getAddress())
                .profileImageUrl(member.getProfileImageUrl())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static MemberProfileResponse fromMemberProfile(Member member,
                                                          Long countFeed,
                                                          Long countFollower,
                                                          Long countFollowing){
        return MemberProfileResponse.builder()
                .nickname(member.getNickname())
                .accountId(member.getAccountId())
                .countFeed(countFeed)
                .countFollower(countFollower)
                .countFollowing(countFollowing)
                .profileImageUrl(member.getProfileImageUrl())
                .isFollow(false)
                .isBlock(false)
                .build();
    }

}