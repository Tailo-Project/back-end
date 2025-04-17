package com.growith.tailo.feed.hashtag.dto;

import jakarta.validation.constraints.NotBlank;

public record HashtagDto(
        @NotBlank(message = "해시태그는 비어 있을 수 없습니다.")
        String hashtagName
) {
}