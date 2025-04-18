package com.growith.tailo.feed.feed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedPost is a Querydsl query type for FeedPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedPost extends EntityPathBase<FeedPost> {

    private static final long serialVersionUID = -1830738327L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedPost feedPost = new QFeedPost("feedPost");

    public final com.growith.tailo.common.entity.QBaseTime _super = new com.growith.tailo.common.entity.QBaseTime(this);

    public final com.growith.tailo.member.entity.QMember author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFeedPost(String variable) {
        this(FeedPost.class, forVariable(variable), INITS);
    }

    public QFeedPost(Path<? extends FeedPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedPost(PathMetadata metadata, PathInits inits) {
        this(FeedPost.class, metadata, inits);
    }

    public QFeedPost(Class<? extends FeedPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.growith.tailo.member.entity.QMember(forProperty("author")) : null;
    }

}

