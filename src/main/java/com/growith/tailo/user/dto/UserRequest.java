package com.growith.tailo.user.dto;


import com.growith.tailo.auth.oauth.entity.OAuthProvider;
import com.growith.tailo.user.entity.User;
import com.growith.tailo.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    private String profileImageUrl;     // Optional

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력하세요.")
    private String nickName;
    @Size(min = 8, max = 100, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;    // Optional

    public User toEntity(String encryptedPassword) {
        return User.builder()
                .role(Role.USER)
                .email(this.email)
                .profileImageUrl(this.profileImageUrl)
                .nickName(this.nickName)
                .password(encryptedPassword)
                .provider(OAuthProvider.LOCAL)
                .emailVerified(true)
                .build();
    }
}
