package com.growith.tailo.member.dto.request;

import com.growith.tailo.member.enums.GenderType;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public record SignUpRequest(String email, String accountId, String nickname, String type, String breed, Integer age, GenderType gender, String address) {
}
