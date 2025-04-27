package com.growith.tailo.feed.feedImage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class MemberFeedImageResponse {
    private Long feedId;
    private String imageUrl;
    private LocalDateTime createdAt;

    @Builder
    public MemberFeedImageResponse(Long feedId, LocalDateTime createdAt) {
        this.feedId = feedId;
        this.imageUrl = "";
        this.createdAt = createdAt;
    }

}
