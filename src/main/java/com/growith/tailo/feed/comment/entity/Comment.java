package com.growith.tailo.feed.comment.entity;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "super_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "feed_post_id")
    private FeedPost feedPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    // getters and setters
}