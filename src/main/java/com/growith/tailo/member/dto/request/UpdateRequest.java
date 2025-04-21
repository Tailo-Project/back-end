package com.growith.tailo.member.dto.request;


import com.growith.tailo.member.enums.GenderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdateRequest(
                             @NotBlank(message = "accountId는 필수 입니다.") String accountId,
                             @NotBlank(message = "닉네임은 필수 입니다.") String nickname,
                             @NotBlank(message = "type은 필수 입니다.") String type,
                             @NotBlank(message = "breed는 필수 입니다.") String breed,
                             @NotNull(message = "age는 필수 입니다.") Integer age,
                             @NotNull(message = "성별은 필수 입니다.") GenderType gender,
                             @NotBlank(message = "주소는 필수 입니다.") String address
                            ){
}
