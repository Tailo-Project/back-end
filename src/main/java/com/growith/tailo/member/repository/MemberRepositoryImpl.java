package com.growith.tailo.member.repository;

import com.growith.tailo.follow.entity.QFollow;
import com.growith.tailo.member.dto.response.MemberProfileResponse;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.entity.QMember;
import com.growith.tailo.member.mapper.from.FromMemberMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberDSLRepository{
    private final JPAQueryFactory queryFactory;


    @Override
    public MemberProfileResponse findByMemberProfile(String accountId) {

        QMember member = QMember.member;
        QFollow follow = QFollow.follow;

        Member result = queryFactory
                .selectFrom(member)
                .where(member.accountId.eq(accountId))
                .fetchOne();
        // 팔로워 수
        Long followerCount = queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.follower.eq(result))
                .fetchOne();
        // 팔로잉 수
        Long followingCount = queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.following.eq(result))
                .fetchOne();

        return FromMemberMapper.fromMemberProfile(result,followerCount,followingCount);

    }
}
