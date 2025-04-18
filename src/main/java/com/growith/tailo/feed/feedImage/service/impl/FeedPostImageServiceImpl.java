package com.growith.tailo.feed.feedImage.service.impl;

import com.growith.tailo.common.handler.ImageUploadHandler;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feedImage.entity.FeedPostImage;
import com.growith.tailo.feed.feedImage.repository.FeedPostImageRepository;
import com.growith.tailo.feed.feedImage.service.FeedPostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedPostImageServiceImpl implements FeedPostImageService {

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

    // 특정 피드의 이미지 목록 조회
    @Override
    public List<String> getImageUrls(FeedPost feedPost) {
        return feedPostImageRepository.findImageUrlsByFeedPost(feedPost);
    }

    // MultipartFile -> Url (클라우드 저장)
    @Override
    public List<String> convertImageToUrls(List<MultipartFile> images) {
        return imageUploadHandler.uploadMultiImages(images);
    }

    // 게시물 수정에 따른 이미지 처리
    @Override
    public void ImageUpdateHandler(List<String> updatedImageUrls, FeedPost feedPost, List<MultipartFile> newImages) {

        List<String> existingImageUrls = getImageUrls(feedPost);
        List<String> newImageUrls = convertImageToUrls(newImages);
        
        if (!existingImageUrls.isEmpty() || existingImageUrls.size() != updatedImageUrls.size()) {
            deleteNotUsedImages(existingImageUrls, updatedImageUrls);
        }

        if (!newImages.isEmpty()) {
            registerImage(newImageUrls, feedPost);
        }
    }

    // 사용하지 않는 이미지 삭제
    private void deleteNotUsedImages(List<String> existingImageUrls, List<String> updatedImages) {
        for (String imageUrl : existingImageUrls) {
            if (updatedImages.contains(imageUrl)) {
                imageUploadHandler.deleteImage(imageUrl);
            }
        }
    }
}
