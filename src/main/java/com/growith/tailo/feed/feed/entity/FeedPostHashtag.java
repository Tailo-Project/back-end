package com.growith.tailo.feed.feed.entity;

import com.growith.tailo.feed.hashtag.entity.Hashtags;
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
@Table(name = "feed_post_hashtag")
public class FeedPostHashtag {
    @Id
    @Column(name = "feed_post_hashtag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feed_post_id")
    private FeedPost feedPost;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtags hashtags;

    @Builder
    public FeedPostHashtag(FeedPost feedPost, Hashtags hashtags) {
        this.feedPost = feedPost;
        this.hashtags = hashtags;
    }
}
