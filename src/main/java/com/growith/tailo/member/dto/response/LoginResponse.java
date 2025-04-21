package com.growith.tailo.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record LoginResponse(String email, String accessToken){
}