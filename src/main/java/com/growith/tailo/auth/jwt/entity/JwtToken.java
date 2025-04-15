package com.growith.tailo.auth.jwt.entity;


import com.growith.tailo.auth.jwt.dto.JwtTokenUpdateRequest;
import com.growith.tailo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "jwt_token", indexes = {
        @Index(name = "idx_jwt_token_user_id", columnList = "user_id"),
        @Index(name = "idx_jwt_token_access_token", columnList = "access_token"),
        @Index(name = "idx_jwt_token_refresh_token", columnList = "refresh_token")
})
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(nullable = false)
    private String tokenType = "Bearer";
    @Column(nullable = false, length = 512)
    private String accessToken;
    @Column(nullable = false, length = 512)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Embedded
    private DeviceInfo deviceInfo;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status = TokenStatus.ACTIVE;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum TokenStatus {
        ACTIVE,
        REVOKED,
        EXPIRED;
    }
    public void revoke() {
        this.status = TokenStatus.REVOKED;
    }

    public void expire() {
        this.status = TokenStatus.EXPIRED;
    }

    public boolean isAccessTokenActive() {
        return this.status == TokenStatus.ACTIVE &&
                Instant.now().isBefore(this.accessTokenExpiresAt);
    }

    public boolean isRefreshTokenActive() {
        return this.status == TokenStatus.ACTIVE &&
                Instant.now().isBefore(this.refreshTokenExpiresAt);
    }

    public JwtToken update(JwtTokenUpdateRequest requestDto) {
        this.deviceInfo = requestDto.getDeviceInfo();
        this.accessToken = requestDto.getAccessToken();
        this.refreshToken = requestDto.getRefreshToken();
        this.accessTokenIssuedAt = requestDto.getAccessTokenIssuedAt();
        this.accessTokenExpiresAt = requestDto.getAccessTokenExpiresAt();
        this.refreshTokenIssuedAt = requestDto.getRefreshTokenIssuedAt();
        this.refreshTokenExpiresAt = requestDto.getRefreshTokenExpiresAt();
        return this;
    }
}
