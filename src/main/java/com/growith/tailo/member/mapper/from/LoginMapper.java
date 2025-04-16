package com.growith.tailo.member.mapper.from;

import com.growith.tailo.member.dto.response.LoginResponse;

public class LoginMapper {
    public static LoginResponse fromEntity(String email, String accessToken){
        return LoginResponse
                .builder()
                .email(email)
                .accessToken(accessToken)
                .build();
    }
}
