package com.growith.tailo.feed.feed.service;

import com.growith.tailo.feed.feed.dto.request.FeedPostRequest;
import com.growith.tailo.feed.feed.dto.request.FeedUpdateRequest;
import com.growith.tailo.feed.feed.dto.response.FeedPostResponse;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedPostService {

    String registerFeedPost(FeedPostRequest feedRegisterRequest, Member member, List<MultipartFile> images);

    String updateFeedPost(Long feedId, FeedUpdateRequest feedUpdateRequest, Member member, List<MultipartFile> images);

    Page<FeedPostResponse> getFeedPostList(Member member, Pageable pageable);

}
