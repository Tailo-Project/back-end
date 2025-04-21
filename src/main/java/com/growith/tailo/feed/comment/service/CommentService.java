package com.growith.tailo.feed.comment.service;

import com.growith.tailo.feed.comment.dto.request.CommentRequest;
import com.growith.tailo.feed.comment.dto.response.CommentResponse;
import com.growith.tailo.feed.comment.dto.response.ReplyResponse;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    String registerComment(Long feedId, CommentRequest commentRequest, Member member);

    String deleteComment(Long feedId, Long commentId, Member member);

    Page<CommentResponse> getCommentList(Long feedId, Member member, Pageable pageable);

    Page<ReplyResponse> getReplyList(Long feedId, Long commentId, Member member, Pageable pageable);
}
