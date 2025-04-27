package com.growith.tailo.feed.feedImage.service;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feedImage.dto.response.MemberFeedImageResponse;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedPostImageService {

    void registerImage(List<String> imageUrls, FeedPost feedPost);

    List<String> getImageUrls(FeedPost feedPost);

    List<String> convertImageToUrls(List<MultipartFile> images);

    void ImageUpdateHandler(List<String> updatedImageUrls, FeedPost feedPost, List<MultipartFile> newImages);

    void deleteImagesByFeedPost(FeedPost feedPost);

    Page<MemberFeedImageResponse> getMemberFeedImageList(Member member, Pageable pageable, String accountId);
}
