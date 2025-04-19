package com.growith.tailo.feed.feedImage.repository;

import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feedImage.entity.QFeedPostImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
}
