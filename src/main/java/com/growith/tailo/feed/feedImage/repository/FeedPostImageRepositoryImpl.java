package com.growith.tailo.feed.feedImage.repository;

import com.growith.tailo.block.entity.QBlockMember;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.entity.QFeedPost;
import com.growith.tailo.feed.feedImage.dto.response.MemberFeedImageResponse;
import com.growith.tailo.feed.feedImage.entity.QFeedPostImage;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FeedPostImageRepositoryImpl implements FeedPostImageCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findImageUrlsByFeedPost(FeedPost feedPost) {
        QFeedPostImage feedPostImage = QFeedPostImage.feedPostImage;

        return queryFactory
                .select(feedPostImage.imageUrl)
                .from(feedPostImage)
                .where(feedPostImage.feedPost.eq(feedPost))
                .fetch();
    }

    @Override
    public Page<MemberFeedImageResponse> getMemberFeedImageList(Member loginMember, Pageable pageable, Long accountId) {

        QFeedPostImage feedPostImage = QFeedPostImage.feedPostImage;
        QFeedPostImage subFeedPostImage = new QFeedPostImage("subFeedPostImage");
        QMember author = QMember.member;
        QFeedPost feedPost = QFeedPost.feedPost;
        QBlockMember blockMember = QBlockMember.blockMember;

        List<MemberFeedImageResponse> content = queryFactory
                .select(Projections.constructor(MemberFeedImageResponse.class,
                        feedPostImage.feedPost.id,
                        feedPostImage.imageUrl,
                        feedPost.createdAt
                ))
                .from(feedPostImage)
                .join(feedPostImage.feedPost, feedPost)
                .join(feedPost.author, author)
                .where(
                        feedPostImage.createdAt.eq(
                                JPAExpressions
                                        .select(subFeedPostImage.createdAt.max())
                                        .from(subFeedPostImage)
                                        .where(subFeedPostImage.feedPost.eq(feedPostImage.feedPost))
                        ),
                        author.id.eq(accountId),
                        author.id.notIn(
                                JPAExpressions.select(blockMember.blocked.id)
                                        .from(blockMember)
                                        .where(blockMember.blocker.id.eq(loginMember.getId()))
                        )
                )
                .orderBy(feedPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(feedPost.count())
                .from(feedPost)
                .join(feedPost.author, author)
                .where(
                        author.id.eq(accountId),
                        author.id.notIn(
                                JPAExpressions.select(blockMember.blocked.id)
                                        .from(blockMember)
                                        .where(blockMember.blocker.id.eq(loginMember.getId()))
                        )
                )
                .fetchOne();

        log.debug("특정 사용자 이미지 조회 - total : " + total + " comments :" + content.size());

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }
}
