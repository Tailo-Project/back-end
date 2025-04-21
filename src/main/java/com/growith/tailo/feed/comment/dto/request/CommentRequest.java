package com.growith.tailo.feed.comment.dto.request;

import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        Long parentId,

        @NotBlank(message = "댓글 내용을 입력하세요.")
        @Size(max = 500, message = "댓글은 500자 이하로 입력해주세요.")
        String content
) {
    public Comment toEntity(FeedPost feedPost, Member author, Comment parentComment, String content) {
        Comment comment = Comment.builder()
                .feedPost(feedPost)
                .parentComment(parentComment)
                .author(author)
                .content(content)
                .build();

        return comment;
    }
}
