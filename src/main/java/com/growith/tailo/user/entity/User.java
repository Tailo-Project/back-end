package com.growith.tailo.user.entity;

import com.growith.tailo.auth.oauth.dto.OAuth2UserInfo;
import com.growith.tailo.auth.oauth.entity.OAuthProvider;
import com.growith.tailo.chat.entity.ChatMessage;
import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.follow.entity.Follow;
import com.growith.tailo.user.enums.GenderType;
import com.growith.tailo.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "members")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;  // 로그인 아이디 or 유저명
    private String nickName;   // 동물 닉네임
    private String type;       // 고양이/강아지 등
    private String breed;
    private int age;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String address;

    private String profileImageUrl;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider provider; // GOOGLE, KAKAO 등

    private String providerId;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 연관관계
    @OneToMany(mappedBy = "sender")
    private List<ChatMessage> sentMessages;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "follower")
    private List<Follow> followers;

    @OneToMany(mappedBy = "following")
    private List<Follow> followings;

    // OAuth 정보 업데이트용
    public User update(OAuth2UserInfo oAuth2UserInfo) {
        this.providerId = oAuth2UserInfo.getId();
        this.email = oAuth2UserInfo.getEmail();
        this.nickName = oAuth2UserInfo.getName();
        this.profileImageUrl = oAuth2UserInfo.getImageUrl();
        return this;
    }
}
