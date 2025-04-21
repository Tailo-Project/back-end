package com.growith.tailo.feed.comment.repository;

import com.growith.tailo.feed.comment.dto.response.CommentResponse;
import com.growith.tailo.feed.comment.dto.response.ReplyResponse;
import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {

    Page<CommentResponse> getCommentList(FeedPost feedPost, Member member, Pageable pageable);

    Page<ReplyResponse> getReplyList(FeedPost feedPost, Comment parentComment, Pageable pageable);
}
