package com.growith.tailo.feed.feed.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedPostRepository extends JpaRepository<FeedPost, Long> {
}
