package com.growith.tailo.member.mapper.to;

import com.growith.tailo.member.dto.request.SignUpRequest;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.enums.Role;
import lombok.*;

@Setter
public class SignUpMapper {
    public static Member toEntity(SignUpRequest signUpRequest){
        return Member.builder()
                .email(signUpRequest.email())
                .accountId(signUpRequest.accountId())
                .nickname(signUpRequest.nickname())
                .type(signUpRequest.type())
                .breed(signUpRequest.breed())
                .age(signUpRequest.age())
                .gender(signUpRequest.gender())
                .address(signUpRequest.address())
                .profileImageUrl(signUpRequest.profileImageUrl())
                .role(Role.USER)
                .build();
    }
}
