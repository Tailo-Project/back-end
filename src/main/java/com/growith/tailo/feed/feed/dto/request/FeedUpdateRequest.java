package com.growith.tailo.feed.feed.dto.request;

import com.growith.tailo.feed.hashtag.dto.HashtagDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record FeedUpdateRequest(
        @NotBlank(message = "피드 본문은 필수입니다")
        @Size(max = 65535, message = "피드 본문은 최대 60000자까지 가능합니다.")
        String content,
        List<HashtagDto> hashtags,
        List<String> imageUrls // 기존에 등록된 이미지
) {
}
