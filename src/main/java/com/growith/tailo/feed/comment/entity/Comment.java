package com.growith.tailo.feed.comment.entity;

import com.growith.tailo.common.entity.BaseTime;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseTime {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "super_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "feed_post_id")
    private FeedPost feedPost;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(Comment parentComment, FeedPost feedPost, Member author, String content) {
        this.parentComment = parentComment;
        this.feedPost = feedPost;
        this.author = author;
        this.content = content;
    }


}