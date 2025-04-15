package com.growith.tailo.feed.likes.entity;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_likes")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private FeedPost feedPost;
    
    // getters and setters
}
