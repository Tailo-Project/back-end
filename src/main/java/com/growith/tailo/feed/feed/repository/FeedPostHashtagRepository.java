package com.growith.tailo.feed.feed.repository;

import com.growith.tailo.feed.feed.entity.FeedPostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedPostHashtagRepository extends JpaRepository<FeedPostHashtag, Long> {
}
