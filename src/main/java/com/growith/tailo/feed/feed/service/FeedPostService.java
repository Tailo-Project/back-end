package com.growith.tailo.feed.feed.service;

import com.growith.tailo.feed.feed.dto.request.FeedPostRequest;
import com.growith.tailo.feed.feed.dto.request.FeedUpdateRequest;
import com.growith.tailo.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedPostService {

    String registerFeedPost(FeedPostRequest feedRegisterRequest, Member member, List<MultipartFile> images);

    String updateFeedPost(Long feedNumber, FeedUpdateRequest feedUpdateRequest, Member member, List<MultipartFile> images);

}
