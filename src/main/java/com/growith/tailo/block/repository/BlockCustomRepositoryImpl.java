package com.growith.tailo.block.repository;

import com.growith.tailo.block.dto.response.BlockResponse;
import com.growith.tailo.block.entity.QBlockMember;
import com.growith.tailo.member.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


@RequiredArgsConstructor
public class BlockCustomRepositoryImpl implements BlockCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BlockResponse> findAllBlockedMember(Member member, Pageable pageable) {
        QBlockMember blockMember = QBlockMember.blockMember;

        List<BlockResponse> content = queryFactory
                .select(Projections.constructor(BlockResponse.class,
                        blockMember.blocked.id,
                        blockMember.blocked.accountId,
                        blockMember.blocked.nickname,
                        blockMember.blocked.profileImageUrl
                ))
                .from(blockMember)
                .join(blockMember.blocked)
                .where(blockMember.blocker.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(blockMember.count())
                .from(blockMember)
                .where(blockMember.blocker.eq(member))
                .fetchOne();
        long total = totalCount != null ? totalCount : 0L;
        return new PageImpl<>(content, pageable, total);
    }
}
