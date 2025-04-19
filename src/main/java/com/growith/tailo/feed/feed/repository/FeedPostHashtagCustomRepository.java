package com.growith.tailo.feed.feed.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.entity.FeedPostHashtag;

import java.util.List;

public interface FeedPostHashtagCustomRepository {

    List<FeedPostHashtag> findHashtagByFeedPost(FeedPost feedPost);
}
