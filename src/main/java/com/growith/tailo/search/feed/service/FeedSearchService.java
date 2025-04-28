package com.growith.tailo.search.feed.service;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.search.feed.dto.FeedSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedSearchService {
    Page<FeedSearchResponse> feedSearch(String keyword, Pageable pageable, Member member);
}
