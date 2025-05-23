package com.growith.tailo.feed.feed.service.impl;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.common.exception.UnauthorizedAccessException;
import com.growith.tailo.feed.comment.repository.CommentRepository;
import com.growith.tailo.feed.feed.dto.request.FeedPostRequest;
import com.growith.tailo.feed.feed.dto.request.FeedUpdateRequest;
import com.growith.tailo.feed.feed.dto.response.FeedPostResponse;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.repository.FeedPostRepository;
import com.growith.tailo.feed.feed.service.FeedPostService;
import com.growith.tailo.feed.feedImage.service.FeedPostImageService;
import com.growith.tailo.feed.hashtag.dto.HashtagDto;
import com.growith.tailo.feed.hashtag.service.HashtagService;
import com.growith.tailo.feed.likes.repository.PostLikeRepository;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FeedPostServiceImpl implements FeedPostService {

    private final FeedPostRepository feedPostRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

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
            List<String> newImageUrls = feedPostImageService.convertImageToUrls(images);
            feedPostImageService.registerImage(newImageUrls, feedPost);
        }

        // 해시태그 저장
        List<HashtagDto> hashtags = postRegisterRequest.hashtags();
        if (!hashtags.isEmpty()) {
            hashtagService.linkHashtagsToPost(hashtags, feedPost);
        }

        return "피드 작성 성공";
    }


    // 나와 나의 팔로우 피드 목록 조회
    @Override
    public Page<FeedPostResponse> getFeedPostList(Member member, Pageable pageable) {
        if (member == null || !memberRepository.existsByAccountId(member.getAccountId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        Page<FeedPostResponse> pageResult = feedPostRepository.getFeedPostList(member, pageable);

        return pageResult;

    }

    // 특정 피드 조회 (상세 조회)
    @Override
    public FeedPostResponse getFeedPost(Long feedId, Member member) {
        if (member == null || !memberRepository.existsByAccountId(member.getAccountId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        FeedPostResponse result = feedPostRepository.getFeedPost(feedId, member);

        return result;
    }

    // 피드 수정
    @Override
    @Transactional
    public String updateFeedPost(Long feedId, FeedUpdateRequest feedUpdateRequest, Member member, List<MultipartFile> images) {

        if (member == null || !memberRepository.existsByAccountId(member.getAccountId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        FeedPost feedPost = feedPostRepository.findById(feedId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 피드가 존재하지 않습니다."));

        if (feedPost.getAuthor().getId() != member.getId()) {
            throw new UnauthorizedAccessException("해당 게시글의 접근 권한이 없습니다.");
        }

        // 이미지 업데이트
        feedPostImageService.ImageUpdateHandler(feedUpdateRequest.imageUrls(), feedPost, images);

        // 피드 업데이트
        feedPost.updateFeed(feedUpdateRequest.content());

        // 해시 업데이트
        List<HashtagDto> newHashtags = feedUpdateRequest.hashtags();
        if (!newHashtags.isEmpty()) {
            hashtagService.updateHashtagHandler(newHashtags, feedPost);
        }

        return "피드 수정 성공";
    }

    // 피드 삭제
    @Override
    @Transactional
    public String deleteFeedPost(Long feedId, Member member) {

        if (member == null || !memberRepository.existsByAccountId(member.getAccountId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        FeedPost feedPost = feedPostRepository.findById(feedId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 피드가 존재하지 않습니다."));

        if (feedPost.getAuthor().getId() != member.getId()) {
            throw new UnauthorizedAccessException("해당 피드의 접근 권한이 없습니다.");
        }

        // 댓글 삭제
        commentRepository.deleteByFeedPost(feedPost);

        // 이미지 삭제
        feedPostImageService.deleteImagesByFeedPost(feedPost);

        // 좋아요 삭제
        postLikeRepository.deleteByFeedPost(feedPost);

        // 해시태그 카운트 감소
        hashtagService.deleteHashtagHandler(feedPost);

        // 게시글 삭제
        feedPostRepository.delete(feedPost);

        return "피드 삭제 성공";

    }


}
