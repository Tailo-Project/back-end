package com.growith.tailo.feed.comment.repository;

import com.growith.tailo.feed.comment.dto.response.CommentResponse;
import com.growith.tailo.feed.comment.dto.response.ReplyListResponse;
import com.growith.tailo.feed.comment.dto.response.ReplyResponse;
import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.feed.comment.entity.QComment;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.member.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentResponse> getCommentList(FeedPost feedPost, Member member, Pageable pageable) {

        QComment comment = QComment.comment;

        // 댓글 조회
        List<CommentResponse> comments = jpaQueryFactory.select(
                        Projections.constructor(CommentResponse.class,
                                comment.id,
                                comment.author.accountId,
                                comment.content,
                                comment.author.nickname,
                                comment.author.profileImageUrl,
                                comment.createdAt
                        ))
                .from(comment)
                .where(comment.feedPost.eq(feedPost)
                        .and(comment.parentComment.isNull())) // 대댓글 제외
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 대댓글 조회 및 매핑
        List<CommentResponse> resultComments = new ArrayList<>();
        for (CommentResponse commentResponse : comments) {
            ReplyListResponse reply = getReplyForComment(commentResponse.commentId());
            resultComments.add(commentResponse.withReplies(reply));
        }

        long total = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.feedPost.eq(feedPost).and(comment.parentComment.isNull()))
                .fetchOne();

        log.debug("total : " + total + " comments :" + comments.size());

        return PageableExecutionUtils.getPage(resultComments, pageable, () -> total);
    }

    @Override
    public Page<ReplyResponse> getReplyList(FeedPost feedPost, Comment parentComment, Pageable pageable) {
        QComment reply = QComment.comment;

        List<ReplyResponse> replies = jpaQueryFactory
                .select(Projections.constructor(ReplyResponse.class,
                        reply.id,
                        reply.content,
                        reply.author.accountId,
                        reply.author.nickname,
                        reply.author.profileImageUrl,
                        reply.createdAt
                ))
                .from(reply)
                .where(reply.parentComment.id.eq(parentComment.getId()))
                .orderBy(reply.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = jpaQueryFactory
                .select(reply.count())
                .from(reply)
                .where(reply.parentComment.id.eq(parentComment.getId()))
                .fetchOne()
                .intValue();

        log.info("total : " + total + " comments :" + replies.size());

        return PageableExecutionUtils.getPage(replies, pageable, () -> total);
    }

    private ReplyListResponse getReplyForComment(Long commentId) {
        QComment reply = QComment.comment;

        // 대댓글 미리보기 3개
        List<ReplyResponse> replies = jpaQueryFactory
                .select(Projections.constructor(ReplyResponse.class,
                        reply.id,
                        reply.content,
                        reply.author.accountId,
                        reply.author.nickname,
                        reply.author.profileImageUrl,
                        reply.createdAt
                ))
                .from(reply)
                .where(reply.parentComment.id.eq(commentId))
                .orderBy(reply.createdAt.desc())
                .limit(3)
                .fetch();

        // 대댓글 수 조회
        int totalCount = jpaQueryFactory
                .select(reply.count())
                .from(reply)
                .where(reply.parentComment.id.eq(commentId))
                .fetchOne()
                .intValue();

        return ReplyListResponse.builder().replies(replies).totalCount(totalCount).build();
    }
}
