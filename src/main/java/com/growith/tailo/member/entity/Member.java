package com.growith.tailo.member.entity;

import com.growith.tailo.chat.entity.ChatMessage;
import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.follow.entity.Follow;
import com.growith.tailo.member.enums.GenderType;
import com.growith.tailo.member.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(unique = true, nullable = false) 
    private String accountId;

    private String nickName;

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

    @OneToMany(mappedBy = "sender")
    private List<ChatMessage> sentMessages;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "follower")
    private List<Follow> followers;

    @OneToMany(mappedBy = "following")
    private List<Follow> followings;
    
    // getters and setters
}
