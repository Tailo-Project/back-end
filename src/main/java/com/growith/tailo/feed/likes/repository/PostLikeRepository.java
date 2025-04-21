package com.growith.tailo.feed.likes.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.likes.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteByFeedPost(FeedPost feedPost);
}
