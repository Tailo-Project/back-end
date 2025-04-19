package com.growith.tailo.member.mapper.to;

import com.growith.tailo.member.dto.request.SignUpRequest;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.enums.Role;
import lombok.Setter;

@Setter
public class ToMemberMapper {

    public static Member signUpToEntity(SignUpRequest signUpRequest, String profileImageUrl){
        return Member.builder()
                .email(signUpRequest.email())
                .accountId(signUpRequest.accountId())
                .nickname(signUpRequest.nickname())
                .type(signUpRequest.type())
                .breed(signUpRequest.breed())
                .age(signUpRequest.age())
                .gender(signUpRequest.gender())
                .address(signUpRequest.address())
                .role(Role.USER)
                .profileImageUrl(profileImageUrl)
                .build();
    }

}
