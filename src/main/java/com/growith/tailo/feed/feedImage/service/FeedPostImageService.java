package com.growith.tailo.feed.feedImage.service;

import com.growith.tailo.feed.feed.entity.FeedPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedPostImageService {

    void registerImage(List<MultipartFile> images, FeedPost feedPost);

}
