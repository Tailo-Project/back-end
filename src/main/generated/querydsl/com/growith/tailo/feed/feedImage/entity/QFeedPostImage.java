package com.growith.tailo.feed.feedImage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedPostImage is a Querydsl query type for FeedPostImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedPostImage extends EntityPathBase<FeedPostImage> {

    private static final long serialVersionUID = -386068941L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedPostImage feedPostImage = new QFeedPostImage("feedPostImage");

    public final com.growith.tailo.feed.feed.entity.QFeedPost feedPost;

    public final NumberPath<Long> feedPostImageId = createNumber("feedPostImageId", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public QFeedPostImage(String variable) {
        this(FeedPostImage.class, forVariable(variable), INITS);
    }

    public QFeedPostImage(Path<? extends FeedPostImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedPostImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedPostImage(PathMetadata metadata, PathInits inits) {
        this(FeedPostImage.class, metadata, inits);
    }

    public QFeedPostImage(Class<? extends FeedPostImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feedPost = inits.isInitialized("feedPost") ? new com.growith.tailo.feed.feed.entity.QFeedPost(forProperty("feedPost"), inits.get("feedPost")) : null;
    }

}

