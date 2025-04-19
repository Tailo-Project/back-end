package com.growith.tailo.feed.feed.entity;

import com.growith.tailo.common.entity.BaseTime;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feed_posts")
public class FeedPost extends BaseTime {
    @Id
    @Column(name = "feed_post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @NotBlank
    @Column(nullable = false)
    private String content;

    @Builder
    public FeedPost(Member author, String content) {
        this.author = author;
        this.content = content;
    }

    public void updateFeed(String content) {
        this.content = content;
    }
}
