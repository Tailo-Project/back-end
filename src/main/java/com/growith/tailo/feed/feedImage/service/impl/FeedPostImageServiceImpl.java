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

    @Override
    @Transactional
    public void registerImage(List<MultipartFile> images, FeedPost feedPost) {
        List<String> imageUrls = imageUploadHandler.uploadMultiImages(images);
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
}
