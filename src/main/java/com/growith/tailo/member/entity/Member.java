package com.growith.tailo.member.entity;

import com.growith.tailo.chat.entity.ChatMessage;
import com.growith.tailo.common.entity.BaseTime;
import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.follow.entity.Follow;
import com.growith.tailo.member.dto.request.UpdateRequest;
import com.growith.tailo.member.enums.GenderType;
import com.growith.tailo.member.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "members")
public class Member extends BaseTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(nullable = false)
    private String address;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<ChatMessage> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String accountId, String nickname, String type, String breed, Integer age, GenderType gender, String address, String profileImageUrl, Role role) {
        this.id = id;
        this.email = email;
        this.accountId = accountId;
        this.nickname = nickname;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Spring Security 에서 사용자의 권한 목록을 반환하는 메서드
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // SimpleGrantedAuthority - 권한을 문자열로 표현한 객체
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
        return authorities;
    }

    @Override
    public String getPassword() { // 소셜로그인만 있으므로 비밀번호는 없음
        return "";
    }

    @Override
    public String getUsername() { //유니크 키를 활용해서 인증 아이디 사용
        return accountId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void updateProfile(UpdateRequest updateRequest) {
        this.accountId = updateRequest.accountId();
        this.nickname = updateRequest.nickname();
        this.breed = updateRequest.breed();
        this.type = updateRequest.type();
        this.address = updateRequest.address();
        this.age = updateRequest.age();
        this.gender = updateRequest.gender();
    }

    // getters and setters
}
