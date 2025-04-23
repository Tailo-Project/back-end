package com.growith.tailo.feed.likes.service;

import com.growith.tailo.feed.likes.dto.response.LikedFeedIdsResponse;
import com.growith.tailo.member.entity.Member;

public interface PostLikeService {
    String likeFeedPost(Long feedId, Member member);

    String countLikes(Long feedId, Member member);

    LikedFeedIdsResponse getLikedFeedIds(Member member);
}
