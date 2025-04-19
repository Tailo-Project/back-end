package com.growith.tailo.member.mapper.from;

import com.growith.tailo.member.dto.response.LoginResponse;
import com.growith.tailo.member.dto.response.MemberDetailResponse;
import com.growith.tailo.member.entity.Member;


public class FromMemberMapper {
    public static LoginResponse fromMemberLogin(String email, String accessToken){
        return LoginResponse
                .builder()
                .email(email)
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
}
