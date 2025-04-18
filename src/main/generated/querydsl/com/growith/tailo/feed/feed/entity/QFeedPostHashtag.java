package com.growith.tailo.feed.feed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedPostHashtag is a Querydsl query type for FeedPostHashtag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedPostHashtag extends EntityPathBase<FeedPostHashtag> {

    private static final long serialVersionUID = 852481315L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedPostHashtag feedPostHashtag = new QFeedPostHashtag("feedPostHashtag");

    public final QFeedPost feedPost;

    public final com.growith.tailo.feed.hashtag.entity.QHashtag hashtag;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QFeedPostHashtag(String variable) {
        this(FeedPostHashtag.class, forVariable(variable), INITS);
    }

    public QFeedPostHashtag(Path<? extends FeedPostHashtag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedPostHashtag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedPostHashtag(PathMetadata metadata, PathInits inits) {
        this(FeedPostHashtag.class, metadata, inits);
    }

    public QFeedPostHashtag(Class<? extends FeedPostHashtag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feedPost = inits.isInitialized("feedPost") ? new QFeedPost(forProperty("feedPost"), inits.get("feedPost")) : null;
        this.hashtag = inits.isInitialized("hashtag") ? new com.growith.tailo.feed.hashtag.entity.QHashtag(forProperty("hashtag")) : null;
    }

}

