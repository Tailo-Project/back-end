package com.growith.tailo.block.repository;

import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.block.entity.QBlockMember;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BlockRepositoryImpl implements BlockQueryDSLRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BlockMember> findAllBlockMember(Member member) {
        QBlockMember blockMember = QBlockMember.blockMember;
        QMember blocked = QMember.member;

        return queryFactory
                .select(Projections.constructor(BlockMember.class,  // Constructor 방식으로 변환
                        blocked.id,
                        blocked.accountId,
                        blocked.nickname,
                        blocked.profileImageUrl
                ))
                .from(blockMember)
                .join(blockMember.blocked, blocked)
                .where(blockMember.blocker.eq(member))  // blocker가 주어진 member와 일치하는 경우
                .fetch();
    }
}
