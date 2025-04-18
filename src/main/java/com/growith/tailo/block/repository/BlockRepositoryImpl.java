package com.growith.tailo.block.repository;

import com.growith.tailo.block.dto.response.BlockListResponse;
import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.block.entity.QBlockMember;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.growith.tailo.block.entity.QBlockMember.blockMember;

@RequiredArgsConstructor
public class BlockRepositoryImpl implements BlockQueryDSLRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BlockMember> findAllBlockedMember(Member member) {
        return queryFactory
                .select(blockMember)
                .from(blockMember) // 차단관리 테이블
                .join(blockMember.blocked).fetchJoin() // 차단당한 사용자의 정보를 조인
                .where(blockMember.blocker.eq(member))  // 차단한 사용자가 멤버와 일치하는 경우
                .fetch();
    }
}
