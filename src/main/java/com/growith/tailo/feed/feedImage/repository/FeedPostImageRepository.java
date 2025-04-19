package com.growith.tailo.feed.feedImage.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feedImage.entity.FeedPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedPostImageRepository extends JpaRepository<FeedPostImage, Long>, FeedPostImageCustomRepository {
    List<FeedPostImage> findByFeedPost(FeedPost feedPost);

    int deleteByImageUrl(String imageUrl);

    void deleteByFeedPost(FeedPost feedPost);
}
