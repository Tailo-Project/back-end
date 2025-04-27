package com.growith.tailo.follow.repository;

import com.growith.tailo.follow.dto.response.FollowMeResponse;
import com.growith.tailo.follow.dto.response.MyFollowResponse;
import com.growith.tailo.follow.entity.QFollow;
import com.growith.tailo.member.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class FollowCustomRepositoryImpl implements FollowCustomRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<MyFollowResponse> findAllMyFollow(Member member, Pageable pageable) {
        QFollow follow = QFollow.follow;

        // 내가 팔로우한 사람
        List<MyFollowResponse> content = queryFactory
                .select(Projections.constructor(MyFollowResponse.class, //반환 클래스에 자동 매핑
                        follow.following.id,
                        follow.following.nickname,
                        follow.following.accountId,
                        follow.following.profileImageUrl
                ))
                .from(follow)
                .where(follow.follower.eq(member)) // 내가 팔로우한
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();  // 페이지에 해당하는 데이터 가져오기

        Long totalCount = queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.follower.eq(member))
                .fetchOne();
        long total = totalCount != null ? totalCount : 0L;
        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<FollowMeResponse> findAllFollowMe(Member member, Pageable pageable) {

    QFollow follow = QFollow.follow;
    QFollow subFollow = new QFollow("subFollow");
        // 나를 팔로우한 사람
    List<FollowMeResponse> content = queryFactory
            .select(Projections.constructor(FollowMeResponse.class, //반환 클래스에 자동 매핑
                    follow.follower.id,
                    follow.follower.nickname,
                    follow.follower.accountId,
                    follow.follower.profileImageUrl,
                    JPAExpressions
                            .selectOne()
                            .from(subFollow)
                            .where(subFollow.follower.eq(member)                // 내가
                                    .and(subFollow.following.eq(follow.follower))) // 이 사람을 팔로우하는지
                            .exists()
            ))
            .from(follow)
            .where(follow.following.eq(member))  // 나를 팔로우한 사람들
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        Long  totalCount = queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.following.eq(member))
                .fetchOne();
        long total = totalCount != null ? totalCount : 0L;
        return new PageImpl<>(content, pageable, total);
    }
}