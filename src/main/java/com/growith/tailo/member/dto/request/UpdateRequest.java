package com.growith.tailo.member.dto.request;


import com.growith.tailo.member.enums.GenderType;
import org.springframework.web.multipart.MultipartFile;

public record UpdateRequest(String nickname,
                            String accountId,
                            String breed,
                            String type,
                            Integer age,
                            GenderType gender,
                            String address,
                            MultipartFile file) {
}
