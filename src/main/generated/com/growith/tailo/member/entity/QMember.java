package com.growith.tailo.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 738133905L;

    public static final QMember member = new QMember("member1");

    public final com.growith.tailo.common.entity.QBaseTime _super = new com.growith.tailo.common.entity.QBaseTime(this);

    public final StringPath accountId = createString("accountId");

    public final StringPath address = createString("address");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath breed = createString("breed");

    public final ListPath<com.growith.tailo.feed.comment.entity.Comment, com.growith.tailo.feed.comment.entity.QComment> comments = this.<com.growith.tailo.feed.comment.entity.Comment, com.growith.tailo.feed.comment.entity.QComment>createList("comments", com.growith.tailo.feed.comment.entity.Comment.class, com.growith.tailo.feed.comment.entity.QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final ListPath<com.growith.tailo.follow.entity.Follow, com.growith.tailo.follow.entity.QFollow> followers = this.<com.growith.tailo.follow.entity.Follow, com.growith.tailo.follow.entity.QFollow>createList("followers", com.growith.tailo.follow.entity.Follow.class, com.growith.tailo.follow.entity.QFollow.class, PathInits.DIRECT2);

    public final ListPath<com.growith.tailo.follow.entity.Follow, com.growith.tailo.follow.entity.QFollow> followings = this.<com.growith.tailo.follow.entity.Follow, com.growith.tailo.follow.entity.QFollow>createList("followings", com.growith.tailo.follow.entity.Follow.class, com.growith.tailo.follow.entity.QFollow.class, PathInits.DIRECT2);

    public final EnumPath<com.growith.tailo.member.enums.GenderType> gender = createEnum("gender", com.growith.tailo.member.enums.GenderType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final EnumPath<com.growith.tailo.member.enums.Role> role = createEnum("role", com.growith.tailo.member.enums.Role.class);

    public final ListPath<com.growith.tailo.chat.entity.ChatMessage, com.growith.tailo.chat.entity.QChatMessage> sentMessages = this.<com.growith.tailo.chat.entity.ChatMessage, com.growith.tailo.chat.entity.QChatMessage>createList("sentMessages", com.growith.tailo.chat.entity.ChatMessage.class, com.growith.tailo.chat.entity.QChatMessage.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

