package com.growith.tailo.feed.feed.entity;

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
@Table(name = "feed_post_images")
public class FeedPostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_post_image_id")
    private Long feedPostImageId;

    @ManyToOne
    @JoinColumn(name = "feed_post_id", nullable = false)
    private FeedPost feedPost;

    @NotBlank
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Builder
    public FeedPostImage(FeedPost feedPost, String imageUrl) {
        this.feedPost = feedPost;
        this.imageUrl = imageUrl;
    }
}
