package com.growith.tailo.feed.feed.service;

import com.growith.tailo.feed.feed.dto.request.FeedPostRequest;
import com.growith.tailo.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedPostService {

    String registerFeedPost(FeedPostRequest postRegisterRequest, Member member, List<MultipartFile> images);
}
