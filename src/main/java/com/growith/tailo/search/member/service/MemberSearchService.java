package com.growith.tailo.search.member.service;

import com.growith.tailo.search.member.docs.MemberDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberSearchService {
    Page<MemberDocument> memberSearch(String keyword, Pageable pageable);
}
