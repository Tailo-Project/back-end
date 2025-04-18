package com.growith.tailo.feed.feed.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.entity.FeedPostHashtag;
import com.growith.tailo.feed.feed.entity.QFeedPostHashtag;
import com.growith.tailo.feed.hashtag.entity.QHashtags;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FeedPostHashtagRepositoryCustomImpl implements FeedPostHashtagCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FeedPostHashtag> findHashtagByFeedPost(FeedPost feedPost) {
        QFeedPostHashtag feedPostHashtag = QFeedPostHashtag.feedPostHashtag;
        QHashtags hashtags = QHashtags.hashtags;

        List<FeedPostHashtag> results = queryFactory.select(feedPostHashtag)
                .from(feedPostHashtag)
                .join(feedPostHashtag.hashtags, hashtags)
                .where(feedPostHashtag.feedPost.id.eq(feedPost.getId()))
                .fetch();

        return results;
    }
}
