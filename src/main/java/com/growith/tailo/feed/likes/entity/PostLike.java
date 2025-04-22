package com.growith.tailo.feed.likes.entity;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_likes", uniqueConstraints = {
        // 유저-피드 중복 방지를 위해서 적용
        @UniqueConstraint(columnNames = {"member_id", "feed_id"})
})
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private FeedPost feedPost;

    @Builder
    public PostLike(Member member, FeedPost feedPost) {
        this.member = member;
        this.feedPost = feedPost;
    }

}
