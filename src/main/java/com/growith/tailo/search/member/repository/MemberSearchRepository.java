package com.growith.tailo.search.member.repository;

import com.growith.tailo.search.member.docs.MemberDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberSearchRepository extends ElasticsearchRepository<MemberDocument, Long> {

}
