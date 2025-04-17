package com.growith.tailo.security.jwt.entity;

import com.growith.tailo.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String accountId;

    @Column(nullable = false,length = 500)
    private String token;

    @Column
    private LocalDateTime expiresDate;

    @Builder
    public RefreshToken(String accountId, String token, LocalDateTime createdDate, LocalDateTime expiresDate) {
        this.accountId = accountId;
        this.token = token;
        this.expiresDate = expiresDate;
    }

    // 토큰 업데이트 메서드
    public void updateToken(String token) {
        this.token = token;
    }


}