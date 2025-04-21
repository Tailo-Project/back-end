package com.growith.tailo.member.dto.response;

import com.growith.tailo.member.enums.GenderType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberDetailResponse (String email,
                                    String nickname,
                                    String accountId,
                                    String breed,
                                    String type,
                                    Integer age,
                                    GenderType gender,
                                    String address,
                                    String profileImageUrl,
                                    LocalDateTime createdAt){

}
