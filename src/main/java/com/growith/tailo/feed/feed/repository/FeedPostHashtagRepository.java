package com.growith.tailo.feed.feed.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.entity.FeedPostHashtag;
import com.growith.tailo.feed.hashtag.dto.HashtagDto;
import com.growith.tailo.feed.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedPostHashtagRepository extends JpaRepository<FeedPostHashtag, Long> {
    List<HashtagDto> findAllByFeedPost(FeedPost feedPost);

    void deleteByHashtagAndFeedPost(Hashtag hashtag, FeedPost feedPost);
}
