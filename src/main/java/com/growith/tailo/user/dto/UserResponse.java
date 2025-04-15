package com.growith.tailo.user.dto;


import com.growith.tailo.auth.oauth.entity.OAuthProvider;
import com.growith.tailo.user.entity.User;
import com.growith.tailo.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String profileImageUrl;
    private String nickName;
    private Role role;
    private OAuthProvider provider;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** Custom Constructor, Entity → ResponseDTO 변환 */
    public UserResponse(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.profileImageUrl = entity.getProfileImageUrl();
        this.nickName = entity.getNickName();
        this.role = entity.getRole();
        this.provider = entity.getProvider();
        this.emailVerified = entity.isEmailVerified();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
