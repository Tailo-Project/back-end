package com.growith.tailo.feed.comment.dto.response;

import java.time.LocalDateTime;


public record CommentResponse(
        Long commentId,
        String content,
        String authorNickname,
        String authorProfile,
        LocalDateTime createdAt,
        ReplyListResponse replies
) {

    public CommentResponse(Long commentId, String content, String authorNickname, String authorProfile, LocalDateTime createdAt) {
        this(commentId, content, authorNickname, authorProfile, createdAt, null);
    }
    
    public CommentResponse withReplies(ReplyListResponse replies) {
        return new CommentResponse(commentId, content, authorNickname, authorProfile, createdAt, replies);
    }

}
