package com.growith.tailo.member.dto.request;


import com.growith.tailo.member.enums.GenderType;

public record UpdateRequest(String nickname,
                            String accountId,
                            String breed,
                            String type,
                            Integer age,
                            GenderType gender,
                            String address) {
}
