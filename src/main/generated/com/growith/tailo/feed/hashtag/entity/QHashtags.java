package com.growith.tailo.feed.hashtag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHashtags is a Querydsl query type for Hashtags
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHashtags extends EntityPathBase<Hashtags> {

    private static final long serialVersionUID = 733146688L;

    public static final QHashtags hashtags = new QHashtags("hashtags");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final StringPath hashtag = createString("hashtag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QHashtags(String variable) {
        super(Hashtags.class, forVariable(variable));
    }

    public QHashtags(Path<? extends Hashtags> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHashtags(PathMetadata metadata) {
        super(Hashtags.class, metadata);
    }

}

