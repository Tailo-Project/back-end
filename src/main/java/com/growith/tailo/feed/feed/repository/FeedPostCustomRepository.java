package com.growith.tailo.feed.feed.repository;

import com.growith.tailo.feed.feed.dto.response.FeedPostResponse;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedPostCustomRepository {

    Page<FeedPostResponse> getFeedPostList(Member member, Pageable pageable);

    FeedPostResponse getFeedPost(Long feedId);
}
