package com.growith.tailo.feed.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class FeedPostResponse {

    private Long feedId;
    private String content;
    private String accountId;
    private String authorNickname;
    private String authorProfile;
    private List<String> imageUrls;
    private List<String> hashtags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long likesCount;
    private long commentsCount;
    private boolean isLiked;

    public FeedPostResponse(Long feedId,
                            String content,
                            String accountId,
                            String authorNickname,
                            String authorProfile,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt,
                            long likesCount,
                            long commentsCount,
                            boolean isLiked) {
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
        this.isLiked = isLiked;
    }
}
