package com.growith.tailo.search.feed.controller;

import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.search.feed.dto.FeedSearchResponse;
import com.growith.tailo.search.feed.dto.FeedSearchResponseList;
import com.growith.tailo.search.feed.service.FeedSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Feed Search", description = "피드 검색 API")
@Slf4j
@RestController
@RequestMapping("/api/search/feeds")
@RequiredArgsConstructor
public class FeedSearchController {

    private final FeedSearchService feedSearchService;

    // 피드 검색
    @Operation(
            summary = "피드 검색",
            description = "*keyword*로 피드 본문 또는 해시태그를 찾는다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 검색 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<FeedSearchResponseList>> searchFeeds(
            @RequestParam("keyword") String keyword,
            @AuthenticationPrincipal Member member,
            Pageable pageable) {

        Pageable noSortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<FeedSearchResponse> result = feedSearchService.feedSearch(keyword, noSortPageable, member);

        FeedSearchResponseList response = new FeedSearchResponseList(result.getContent(), new Pagination(
                result.getNumber(),
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(response));
    }
}
