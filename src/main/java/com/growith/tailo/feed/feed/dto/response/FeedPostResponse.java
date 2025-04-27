package com.growith.tailo.feed.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class FeedPostResponse {

    private final Long feedId;
    private final String content;
    private final String accountId;
    String authorNickname;
    String authorProfile;
    List<String> imageUrls;
    List<String> hashtags;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    long likesCount;
    long commentsCount;

    public FeedPostResponse(Long feedId,
                            String content,
                            String accountId,
                            String authorNickname,
                            String authorProfile,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt,
                            long likesCount,
                            long commentsCount) {
        this.feedId = feedId;
        this.content = content;
        this.accountId = accountId;
        this.authorNickname = authorNickname;
        this.authorProfile = authorProfile;
        this.imageUrls = new ArrayList<>();
        this.hashtags = new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
    }
}
