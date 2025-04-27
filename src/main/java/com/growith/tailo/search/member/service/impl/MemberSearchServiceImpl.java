package com.growith.tailo.search.member.service.impl;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.growith.tailo.search.member.docs.MemberDocument;
import com.growith.tailo.search.member.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberSearchServiceImpl implements MemberSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<MemberDocument> memberSearch(String keyword, Pageable pageable) {

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

        // TODO : 팔로우 관계도 넣어줘야 할지도...


        return new PageImpl<>(members, pageable, searchHits.getTotalHits());
    }

}
