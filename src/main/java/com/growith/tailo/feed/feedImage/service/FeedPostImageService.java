package com.growith.tailo.feed.feedImage.service;

import com.growith.tailo.feed.feed.entity.FeedPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedPostImageService {

    void registerImage(List<String> imageUrls, FeedPost feedPost);

    List<String> getImageUrls(FeedPost feedPost);

    List<String> convertImageToUrls(List<MultipartFile> images);

    void ImageUpdateHandler(List<String> updatedImageUrls, FeedPost feedPost, List<MultipartFile> newImages);

    void deleteImagesByFeedPost(FeedPost feedPost);
}
