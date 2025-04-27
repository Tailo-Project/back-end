package com.growith.tailo.search.member.controller;

import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.search.member.docs.MemberDocument;
import com.growith.tailo.search.member.dto.MemberSearchResponse;
import com.growith.tailo.search.member.service.MemberSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member Search", description = "사용자 검색 API")
@Slf4j
@RestController
@RequestMapping("/api/search/members")
@RequiredArgsConstructor
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    // 사용자 검색
    @Operation(
            summary = "사용자 검색",
            description = "*keyword*로 사용자의 accountId 또는 nickname 을 검색합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 검색 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<MemberSearchResponse>> searchMembers(
            @RequestParam("keyword") String keyword,
            Pageable pageable) {

        // 정렬이 안되기 때문에 아예 없애서 새로 만든다.
        Pageable noSortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<MemberDocument> result = memberSearchService.memberSearch(keyword, noSortPageable);

        MemberSearchResponse response = new MemberSearchResponse(result.getContent(), new Pagination(
                result.getNumber(),
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(response));
    }

    // TODO: 자동완성?

}
