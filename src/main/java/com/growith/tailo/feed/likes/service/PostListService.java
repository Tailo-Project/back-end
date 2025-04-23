package com.growith.tailo.feed.likes.service;

import com.growith.tailo.member.entity.Member;

public interface PostListService {
    String likeFeedPost(Long feedId, Member member);

    String countLikes(Long feedId, Member member);
}
