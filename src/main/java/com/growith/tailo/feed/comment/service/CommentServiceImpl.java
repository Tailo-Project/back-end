package com.growith.tailo.feed.comment.service;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.feed.comment.dto.CommentRequest;
import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.feed.comment.repository.CommentRepository;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.repository.FeedPostRepository;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final FeedPostRepository feedPostRepository;

    // 댓글 등록
    @Override
    @Transactional
    public String registerComment(Long feedId, CommentRequest commentRequest, Member member) {

        if (member == null || !memberRepository.existsByAccountId(member.getAccountId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        FeedPost feedPost = feedPostRepository.findById(feedId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 피드입니다."));

        Long parentId = commentRequest.parentId();
        Comment parentComment = null;
        if (parentId != null) {
            parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 부모 댓글입니다."));

            if (!parentComment.getFeedPost().getId().equals(feedId)) {
                throw new IllegalArgumentException("부모 댓글이 해당 피드의 댓글이 아닙니다.");
            }
        }

        boolean isDuplicate = commentRepository.existsByAuthorAndFeedPostAndContentAndCreatedAtAfter(
                member, feedPost, commentRequest.content(), LocalDateTime.now().minusMinutes(1));

        Comment comment = commentRequest.toEntity(feedPost, member, parentComment, commentRequest.content());
        commentRepository.save(comment);

        return "댓글 등록 성공";
    }

    // 댓글 삭제
    @Override
    @Transactional
    public String deleteComment(Long feedId, Long commentId, Member member) {

        if (member == null || !memberRepository.existsByAccountId(member.getAccountId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        FeedPost feedPost = feedPostRepository.findById(feedId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 피드입니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 댓글입니다."));

        if (!comment.getFeedPost().getId().equals(feedId)) {
            throw new IllegalArgumentException("요청한 피드와 댓글이 속한 피드가 일치하지 않습니다.");
        }

        if (!comment.getAuthor().getId().equals(member.getId())) {
            throw new IllegalArgumentException("댓글 삭제할 권한이 없습니다.");
        }

        // 대댓글 삭제
        List<Comment> childComments = commentRepository.findByParentComment(comment);
        commentRepository.deleteAll(childComments);

        // 댓글 삭제
        commentRepository.deleteById(commentId);

        return "댓글 삭제 성공";
    }
}
