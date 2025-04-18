package com.growith.tailo.feed.feed.service.impl;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.feed.feed.dto.request.FeedPostRequest;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.repository.FeedPostRepository;
import com.growith.tailo.feed.feed.service.FeedPostService;
import com.growith.tailo.feed.feedImage.service.FeedPostImageService;
import com.growith.tailo.feed.hashtag.dto.HashtagDto;
import com.growith.tailo.feed.hashtag.service.HashtagService;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedPostServiceImpl implements FeedPostService {

    private final FeedPostRepository feedPostRepository;
    private final MemberRepository memberRepository;

    private final HashtagService hashtagService;
    private final FeedPostImageService feedPostImageService;

    // 피드 등록
    @Override
    @Transactional
    public String registerFeedPost(FeedPostRequest postRegisterRequest, Member member, List<MultipartFile> images) {

        if (member == null || !memberRepository.existsByAccountId(member.getAccountId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        // 피드 저장
        FeedPost feedPost = FeedPost.builder()
                .author(member)
                .content(postRegisterRequest.content())
                .build();

        feedPostRepository.save(feedPost);

        // 이미지 파일 저장
        if (images != null && !images.isEmpty()) {
            feedPostImageService.registerImage(images, feedPost);
        }

        // 해시태그 저장
        List<HashtagDto> hashtags = postRegisterRequest.hashtags();
        if (hashtags.size() > 0) {
            hashtagService.linkHashtagsToPost(hashtags, feedPost);
        }

        return "피드 작성 성공";
    }
}
