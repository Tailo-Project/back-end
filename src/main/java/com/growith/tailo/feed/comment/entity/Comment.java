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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseTime {
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
    private Member user;

    @Column(nullable = false)
    private String content;


}