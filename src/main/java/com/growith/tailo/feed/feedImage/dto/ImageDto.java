package com.growith.tailo.feed.feedImage.dto;

import jakarta.validation.constraints.NotBlank;

public record ImageDto(
        @NotBlank(message = "이미지 URL은 필수입니다.")
        String imageUrl
) {
}