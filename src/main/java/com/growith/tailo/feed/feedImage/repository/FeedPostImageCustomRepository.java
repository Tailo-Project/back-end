package com.growith.tailo.feed.feedImage.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feedImage.dto.response.MemberFeedImageResponse;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedPostImageCustomRepository {
    List<String> findImageUrlsByFeedPost(FeedPost feedPost);

    Page<MemberFeedImageResponse> getMemberFeedImageList(Member loginMember, Pageable pageable, Long accountId);
}
