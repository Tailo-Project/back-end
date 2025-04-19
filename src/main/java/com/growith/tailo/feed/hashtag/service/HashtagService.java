package com.growith.tailo.feed.hashtag.service;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.hashtag.dto.HashtagDto;

import java.util.List;

public interface HashtagService {
    void linkHashtagsToPost(List<HashtagDto> hashtagList, FeedPost feedPost);

    void updateHashtagHandler(List<HashtagDto> updatedHashtags, FeedPost feedPost);
}
