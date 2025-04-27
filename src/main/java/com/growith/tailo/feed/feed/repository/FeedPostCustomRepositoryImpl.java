package com.growith.tailo.feed.feed.repository;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.feed.comment.entity.QComment;
import com.growith.tailo.feed.feed.dto.response.FeedPostResponse;
import com.growith.tailo.feed.feed.entity.QFeedPost;
import com.growith.tailo.feed.feed.entity.QFeedPostHashtag;
import com.growith.tailo.feed.feedImage.entity.QFeedPostImage;
import com.growith.tailo.feed.hashtag.entity.QHashtags;
import com.growith.tailo.feed.likes.entity.QPostLike;
import com.growith.tailo.follow.entity.QFollow;
import com.growith.tailo.member.entity.Member;
import com.querydsl.core.Tuple;
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

        // 피드 페이징
        List<Long> feedPostIds = jpaQueryFactory
                .select(feedPost.id)
                .distinct()
                .from(feedPost)
                .leftJoin(follow).on(follow.follower.id.eq(member.getId()))
                .where(
                        feedPost.author.id.eq(member.getId())
                                .or(follow.follower.id.eq(member.getId())
                                        .and(follow.following.id.eq(feedPost.author.id)))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 페이징한 피드 아이디를 통해 피드에 필요한 정보 조회
        Map<Long, FeedPostResponse> feedMap = jpaQueryFactory
                .from(feedPost)
                .where(feedPost.id.in(feedPostIds))
                .orderBy(feedPost.createdAt.desc(), feedPost.id.desc())
                .transform(GroupBy.groupBy(feedPost.id).as(
                        Projections.constructor(FeedPostResponse.class,
                                feedPost.id,
                                feedPost.content,
                                feedPost.author.accountId,
                                feedPost.author.nickname,
                                feedPost.author.profileImageUrl,
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

        // 이미지 목록 가져오기
        insertImages(feedImage, feedMap);

        // 해시태그 목록 가져오기
        insertHashTags(hashtags, feedPostHashtag, feedMap);

        Long total = jpaQueryFactory
                .select(feedPost.countDistinct())
                .from(feedPost)
                .leftJoin(follow).on(follow.follower.id.eq(member.getId()))
                .where(
                        feedPost.author.id.eq(member.getId())
                                .or(follow.follower.id.eq(member.getId())
                                        .and(follow.following.id.eq(feedPost.author.id)))
                )
                .fetchOne();

        log.info("offset: " + pageable.getOffset() + " limit: " + pageable.getPageSize() + " 피드 리스트 갯수 : " + feeds.size() + " 실제 count 갯수: " + total);

        return PageableExecutionUtils.getPage(feeds, pageable, () -> total);
    }

    private void insertHashTags(QHashtags hashtags, QFeedPostHashtag feedPostHashtag, Map<Long, FeedPostResponse> feedMap) {
        List<Tuple> hashtagResults = jpaQueryFactory
                .select(feedPostHashtag.feedPost.id, hashtags.hashtag)
                .from(feedPostHashtag)
                .leftJoin(hashtags).on(hashtags.id.eq(feedPostHashtag.hashtags.id))
                .where(feedPostHashtag.feedPost.id.in(feedMap.keySet()))
                .fetch();

        for (Tuple hashtagResult : hashtagResults) {
            Long feedId = hashtagResult.get(feedPostHashtag.feedPost.id);
            String hashtag = hashtagResult.get(hashtags.hashtag);
            FeedPostResponse feedPost = feedMap.get(feedId);
            feedPost.getHashtags().add(hashtag);
        }
    }

    private void insertImages(QFeedPostImage feedImage, Map<Long, FeedPostResponse> feedMap) {
        List<Tuple> imageResults = jpaQueryFactory
                .select(feedImage.feedPost.id, feedImage.imageUrl)
                .from(feedImage)
                .where(feedImage.feedPost.id.in(feedMap.keySet()))
                .fetch();

        for (Tuple imageResult : imageResults) {
            Long feedId = imageResult.get(feedImage.feedPost.id);
            String imageUrl = imageResult.get(feedImage.imageUrl);
            FeedPostResponse feedPost = feedMap.get(feedId);
            feedPost.getImageUrls().add(imageUrl);
        }
    }

    @Override
    public FeedPostResponse getFeedPost(Long feedId) {
        QFeedPost feedPost = QFeedPost.feedPost;
        QComment comment = QComment.comment;
        QPostLike postLike = QPostLike.postLike;
        QHashtags hashtags = QHashtags.hashtags;
        QFeedPostImage feedImage = QFeedPostImage.feedPostImage;
        QFeedPostHashtag feedPostHashtag = QFeedPostHashtag.feedPostHashtag;

        FeedPostResponse result = jpaQueryFactory
                .from(feedPost)
                .leftJoin(feedImage).on(feedImage.feedPost.eq(feedPost))
                .leftJoin(feedPostHashtag).on(feedPostHashtag.feedPost.eq(feedPost))
                .leftJoin(hashtags).on(hashtags.id.eq(feedPostHashtag.hashtags.id))
                .where(
                        feedPost.id.eq(feedId)
                )
                .transform(GroupBy.groupBy(feedPost.id).as(
                        Projections.constructor(FeedPostResponse.class,
                                feedPost.id,
                                feedPost.content,
                                feedPost.author.accountId,
                                feedPost.author.nickname,
                                feedPost.author.profileImageUrl,
                                GroupBy.list(feedImage.imageUrl),
                                GroupBy.list(hashtags.hashtag),
                                feedPost.createdAt,
                                feedPost.updatedAt,
                                JPAExpressions.select(postLike.count())
                                        .from(postLike)
                                        .where(postLike.feedPost.eq(feedPost)),
                                JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.feedPost.eq(feedPost))
                        )
                )).get(feedId);

        if (result == null) {
            throw new ResourceNotFoundException("해당 피드가 존재하지 않습니다.");
        }

        return result;
    }
}