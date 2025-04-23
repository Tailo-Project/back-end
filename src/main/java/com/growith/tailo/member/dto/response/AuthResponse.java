package com.growith.tailo.member.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(String email, String accountId, String accessToken){
}