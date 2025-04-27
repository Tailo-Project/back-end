package com.growith.tailo.feed.feedImage.service.impl;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.common.handler.ImageUploadHandler;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feedImage.dto.response.MemberFeedImageResponse;
import com.growith.tailo.feed.feedImage.entity.FeedPostImage;
import com.growith.tailo.feed.feedImage.repository.FeedPostImageRepository;
import com.growith.tailo.feed.feedImage.service.FeedPostImageService;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FeedPostImageServiceImpl implements FeedPostImageService {

    private final MemberRepository memberRepository;
    private final FeedPostImageRepository feedPostImageRepository;

    private final ImageUploadHandler imageUploadHandler;

    // 이미지 등록
    @Override
    @Transactional
    public void registerImage(List<String> imageUrls, FeedPost feedPost) {
        List<FeedPostImage> feedPostImages = new ArrayList<>();

        for (String imageUrl : imageUrls) {
            FeedPostImage feedPostImage = FeedPostImage.builder()
                    .feedPost(feedPost)
                    .imageUrl(imageUrl)
                    .build();

            feedPostImages.add(feedPostImage);
        }

        feedPostImageRepository.saveAll(feedPostImages);
    }

    // MultipartFile -> Url (클라우드 저장)
    @Override
    public List<String> convertImageToUrls(List<MultipartFile> images) {
        return imageUploadHandler.uploadMultiImages(images);
    }

    // 특정 피드의 이미지 목록 조회
    @Override
    public List<String> getImageUrls(FeedPost feedPost) {
        return feedPostImageRepository.findImageUrlsByFeedPost(feedPost);
    }

    // 특정 사용자 피드 이미지 목록 조회
    @Override
    public Page<MemberFeedImageResponse> getMemberFeedImageList(Member member, Pageable pageable, String accountId) {

        if (!memberRepository.existsByAccountId(accountId)) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        return feedPostImageRepository.getMemberFeedImageList(member, pageable, accountId);
    }

    // 게시물 수정에 따른 이미지 처리
    @Override
    public void ImageUpdateHandler(List<String> updatedImageUrls, FeedPost feedPost, List<MultipartFile> newImages) {

        List<String> existingImageUrls = getImageUrls(feedPost);
        List<String> newImageUrls = convertImageToUrls(newImages);

        deleteNotUsedImages(existingImageUrls, updatedImageUrls);

        int remainImagesCount = updatedImageUrls.size();
        int newImagesCount = newImageUrls.size();

        if (remainImagesCount + newImagesCount > 4) {
            throw new IllegalArgumentException("이미지는 최대 4개까지만 등록할 수 있습니다.");
        }

        if (!newImages.isEmpty()) {
            registerImage(newImageUrls, feedPost);
        }
    }

    // 이미지 삭제
    @Override
    @Transactional
    public void deleteImagesByFeedPost(FeedPost feedPost) {
        List<FeedPostImage> images = feedPostImageRepository.findByFeedPost(feedPost);

        for (FeedPostImage image : images) {
            imageUploadHandler.deleteImage(image.getImageUrl());
        }
        feedPostImageRepository.deleteByFeedPost(feedPost);
    }

    // 사용하지 않는 이미지 삭제
    private void deleteNotUsedImages(List<String> existingImageUrls, List<String> updatedImages) {
        for (String imageUrl : existingImageUrls) {
            if (!updatedImages.contains(imageUrl)) {
                // DB에서 삭제
                int cnt = feedPostImageRepository.deleteByImageUrl(imageUrl);
                // 이미지 저장소에서 삭제
                // TODO : 트랜젝션이 중간에 실패해도 반영되기 때문에 이벤트 처리 필요
                imageUploadHandler.deleteImage(imageUrl);

                log.debug("사용하지 않은 이미지 삭제 처리");
            }
        }
    }
}
