package com.growith.tailo.feed.feed.entity;

import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feed_posts")
public class FeedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "feedPost")
    private List<Comment> comments;

    @OneToMany(mappedBy = "feedPost")
    private List<FeedPostHashtag> hashtags;
    
    // getters and setters
}
