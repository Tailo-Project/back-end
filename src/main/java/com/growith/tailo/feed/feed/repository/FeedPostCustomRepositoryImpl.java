package com.growith.tailo.feed.feed.repository;

import com.growith.tailo.feed.comment.entity.QComment;
import com.growith.tailo.feed.feed.dto.response.FeedPostResponse;
import com.growith.tailo.feed.feed.entity.QFeedPost;
import com.growith.tailo.feed.feed.entity.QFeedPostHashtag;
import com.growith.tailo.feed.feedImage.entity.QFeedPostImage;
import com.growith.tailo.feed.hashtag.entity.QHashtags;
import com.growith.tailo.feed.likes.entity.QPostLike;
import com.growith.tailo.follow.entity.QFollow;
import com.growith.tailo.member.entity.Member;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class FeedPostCustomRepositoryImpl implements FeedPostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FeedPostResponse> getFeedPostList(Member member, Pageable pageable) {

        QFeedPost feedPost = QFeedPost.feedPost;
        QComment comment = QComment.comment;
        QPostLike postLike = QPostLike.postLike;
        QHashtags hashtags = QHashtags.hashtags;
        QFeedPostImage feedImage = QFeedPostImage.feedPostImage;
        QFeedPostHashtag feedPostHashtag = QFeedPostHashtag.feedPostHashtag;
        QFollow follow = QFollow.follow;

        Map<Long, FeedPostResponse> feedMap = jpaQueryFactory
                .from(feedPost)
                .leftJoin(feedImage).on(feedImage.feedPost.eq(feedPost))
                .leftJoin(feedPostHashtag).on(feedPostHashtag.feedPost.eq(feedPost))
                .leftJoin(hashtags).on(hashtags.id.eq(feedPostHashtag.hashtags.id))
                .leftJoin(follow).on(follow.follower.id.eq(member.getId()).or(follow.following.id.eq(member.getId())))
                .where(
                        feedPost.author.id.eq(member.getId()).or(follow.follower.eq(member))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(GroupBy.groupBy(feedPost.id).as(
                        Projections.constructor(FeedPostResponse.class,
                                feedPost.id,
                                feedPost.content,
                                feedPost.author.nickname,
                                feedPost.author.profileImageUrl,
                                GroupBy.list(feedImage.imageUrl), // 이미지들 묶기
                                GroupBy.list(hashtags.hashtag),   // 해시태그 묶기
                                feedPost.createdAt,
                                feedPost.updatedAt,
                                JPAExpressions.select(postLike.count())
                                        .from(postLike)
                                        .where(postLike.feedPost.eq(feedPost)),
                                JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.feedPost.eq(feedPost))
                        )
                ));

        List<FeedPostResponse> feeds = new ArrayList<>(feedMap.values());

        Long total = jpaQueryFactory
                .select(feedPost.count())
                .from(feedPost)
                .leftJoin(follow).on(follow.follower.id.eq(member.getId()).or(follow.following.id.eq(member.getId())))
                .where(
                        feedPost.author.id.eq(member.getId()).or(follow.follower.eq(member))
                )
                .fetchOne();

        return PageableExecutionUtils.getPage(feeds, pageable, () -> total);
    }
}
