package com.growith.tailo.member.entity;

import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.chat.entity.ChatMessage;
import com.growith.tailo.common.entity.BaseTime;
import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.follow.entity.Follow;
import com.growith.tailo.member.dto.request.UpdateRequest;
import com.growith.tailo.member.enums.GenderType;
import com.growith.tailo.member.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "members")
public class Member extends BaseTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    //@Column(unique = true, nullable = false)
    private String accountId;

    private String nickname;

    private String type;

    private String breed;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String address;

    private String profileImageUrl;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<ChatMessage> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Comment> comments =new ArrayList<>();

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Follow> followers= new ArrayList<>();

    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Follow> followings=new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Spring Security 에서 사용자의 권한 목록을 반환하는 메서드
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // SimpleGrantedAuthority - 권한을 문자열로 표현한 객체
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.role.name()));
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
        this.type=updateRequest.type();
        this.address=updateRequest.address();
        this.age=updateRequest.age();
        this.gender=updateRequest.gender();
    }

    // getters and setters
}
