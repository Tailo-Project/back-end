package com.growith.tailo.feed.feedImage.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;

import java.util.List;

public interface FeedPostImageCustomRepository {
    List<String> findImageUrlsByFeedPost(FeedPost feedPost);
}
