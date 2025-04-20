package com.growith.tailo.feed.comment.service;

import com.growith.tailo.feed.comment.dto.CommentRequest;
import com.growith.tailo.member.entity.Member;

public interface CommentService {
    String registerComment(Long feedId, CommentRequest commentRequest, Member member);
}
