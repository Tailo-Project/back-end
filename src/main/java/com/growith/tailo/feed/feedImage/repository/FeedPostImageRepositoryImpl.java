package com.growith.tailo.feed.feedImage.repository;

import com.growith.tailo.block.entity.QBlockMember;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.entity.QFeedPost;
import com.growith.tailo.feed.feedImage.dto.response.MemberFeedImageResponse;
import com.growith.tailo.feed.feedImage.entity.QFeedPostImage;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.entity.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Page<MemberFeedImageResponse> getMemberFeedImageList(Member loginMember, Pageable pageable, String accountId) {

        QFeedPostImage feedPostImage = QFeedPostImage.feedPostImage;
        QFeedPostImage subFeedPostImage = new QFeedPostImage("subFeedPostImage");
        QMember author = QMember.member;
        QFeedPost feedPost = QFeedPost.feedPost;
        QBlockMember blockMember = QBlockMember.blockMember;

        // feedId, CreatedAt 정보 가져오기
        List<Tuple> feeds = fetchFeeds(loginMember, pageable, accountId, author, feedPost, blockMember);

        // 맵 구성
        Map<Long, MemberFeedImageResponse> feedMap = buildFeedMap(feedPost, feeds);

        // feeds의 이미지 url 정보 가져오기
        List<Tuple> imageUrlResults = fetchImageUrls(feedPostImage, subFeedPostImage, author, feedPost, feedMap);

        for (Tuple imageUrlResult : imageUrlResults) {
            Long feedId = imageUrlResult.get(feedPost.id);
            String imageUrl = imageUrlResult.get(feedPostImage.imageUrl);

            log.info(feedId + " " + imageUrl);

            MemberFeedImageResponse memberFeedImage = feedMap.get(feedId);
            memberFeedImage.setImageUrl(imageUrl);
        }

        List<MemberFeedImageResponse> content = new ArrayList<>(feedMap.values());

        // 전체 카운트 조회
        Long total = queryFactory
                .select(feedPost.count())
                .from(feedPost)
                .join(feedPost.author, author)
                .where(
                        author.accountId.eq(accountId),
                        author.id.notIn(
                                JPAExpressions.select(blockMember.blocked.id)
                                        .from(blockMember)
                                        .where(blockMember.blocker.id.eq(loginMember.getId()))
                        )
                )
                .fetchOne();

        log.debug("특정 사용자 이미지 조회 수 : " + total + " 현재 페이지 데이터 수 :" + content.size());

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    private List<Tuple> fetchImageUrls(QFeedPostImage feedPostImage, QFeedPostImage subFeedPostImage, QMember author, QFeedPost feedPost, Map<Long, MemberFeedImageResponse> feedMap) {
        return queryFactory
                .select(feedPost.id, feedPostImage.imageUrl)
                .from(feedPostImage)
                .join(feedPostImage.feedPost, feedPost)
                .join(feedPost.author, author)
                .where(
                        feedPostImage.createdAt.eq(
                                JPAExpressions
                                        .select(subFeedPostImage.createdAt.max()) // 최신 이미지를 조회
                                        .from(subFeedPostImage)
                                        .where(subFeedPostImage.feedPost.eq(feedPostImage.feedPost))
                        ),
                        feedPostImage.feedPost.id.in(feedMap.keySet())
                )
                .fetch();
    }

    private Map<Long, MemberFeedImageResponse> buildFeedMap(QFeedPost feedPost, List<Tuple> feeds) {
        Map<Long, MemberFeedImageResponse> feedMap = new HashMap<>();

        for (Tuple feed : feeds) {
            Long feedId = feed.get(QFeedPost.feedPost.id);
            LocalDateTime createdAt = feed.get(feedPost.createdAt);

            log.info(feedId + " " + createdAt);

            feedMap.put(feedId,
                    MemberFeedImageResponse.builder()
                            .feedId(feedId)
                            .createdAt(createdAt)
                            .build());
        }

        return feedMap;
    }

    private List<Tuple> fetchFeeds(Member loginMember, Pageable pageable, String accountId, QMember author, QFeedPost feedPost, QBlockMember blockMember) {
        return queryFactory
                .select(feedPost.id, feedPost.createdAt)
                .from(feedPost)
                .join(feedPost.author, author)
                .where(
                        author.accountId.eq(accountId),
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
    }
}
