package com.growith.tailo.search.member.service;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.search.member.dto.MemberSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberSearchService {
    Page<MemberSearchResponse> memberSearch(String keyword, Pageable pageable, Member member);
}
