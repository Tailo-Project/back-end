package com.growith.tailo.search.member.service.impl;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.block.repository.BlockRepository;
import com.growith.tailo.follow.entity.Follow;
import com.growith.tailo.follow.repository.FollowRepository;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.search.member.docs.MemberDocument;
import com.growith.tailo.search.member.dto.MemberSearchResponse;
import com.growith.tailo.search.member.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSearchServiceImpl implements MemberSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final BlockRepository blockRepository;
    private final FollowRepository followRepository;

    @Override
    public Page<MemberSearchResponse> memberSearch(String keyword, Pageable pageable, Member member) {

        // 와일드 카드 설정
        String wildcardKeyword = "*" + keyword + "*";

        // 계정 아이디 또는 닉네임이 검색어에 포함되면 검색 결과에 포함되도록 한다.
        // 참고 : 완전일치 wildcard -> match
        Query query = QueryBuilders.bool(b -> b
                .should(s -> s.wildcard(m -> m.field("account_id").value(wildcardKeyword)))
                .should(s -> s.wildcard(m -> m.field("nickname").value(wildcardKeyword)))
        );

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query) // 검색 조건
                .withPageable(pageable) // 페이징 설정
                .withSort(s -> s.field(f -> f.field("updated_at").order(SortOrder.Desc))) // 최신순
                .build();

        // 실제로 ElasticSearch에 쿼리를 실행하여 결과 반환
        SearchHits<MemberDocument> searchHits = elasticsearchOperations.search(nativeQuery, MemberDocument.class);

        // MemberDocument(getContent) 추출하여 List로 반환
        List<MemberDocument> members = searchHits.get().map(SearchHit::getContent).toList();

        List<Long> memberIds = members.stream().map(MemberDocument::getId).toList();

        // 팔로우, 차단 관계 조회
        List<Follow> follows = followRepository.findByFollowerIdAndFollowingIdIn(member.getId(), memberIds);
        List<BlockMember> blocks = blockRepository.findByBlockerIdAndBlockedIdIn(member.getId(), memberIds);

        List<Long> blockIds = blocks.stream()
                .map(block -> block.getBlocked().getId())
                .toList();

        List<Long> followIds = follows.stream()
                .map(follow -> follow.getFollower().getId())
                .toList();

        // 차단한 사용자는 결과에서 제외, 팔로우 여부 표시
        List<MemberSearchResponse> responses = members.stream()
                .filter(m -> !blockIds.contains(m.getId()))
                .map(m -> MemberSearchResponse.builder()
                        .id(m.getId())
                        .accountId(m.getAccountId())
                        .nickname(m.getNickname())
                        .profileImageUrl(m.getProfileImageUrl())
                        .createdAt(m.getCreatedAt())
                        .updatedAt(m.getUpdatedAt())
                        .isFollowing(followIds.contains(m.getId()))
                        .build())
                .toList();

        if (responses.isEmpty()) {
            log.info("사용자 검색 결과 없음: keyword={}", keyword);
        } else {
            log.info("검색완료: keyword={}, 결과 수={}, 제외된 수={}", keyword, responses.size(), responses.size() - members.size());
        }

        return new PageImpl<>(responses, pageable, searchHits.getTotalHits());
    }

}
