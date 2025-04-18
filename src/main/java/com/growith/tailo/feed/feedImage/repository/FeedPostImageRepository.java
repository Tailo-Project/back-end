package com.growith.tailo.feed.feedImage.repository;

import com.growith.tailo.feed.feedImage.entity.FeedPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedPostImageRepository extends JpaRepository<FeedPostImage, Long>, FeedPostImageCustomRepository {
    void deleteByImageUrl(String imageUrl);
}
