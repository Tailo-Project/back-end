package com.growith.tailo.feed.feedImage.controller;

import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.feed.feedImage.dto.response.MemberFeedImageListResponse;
import com.growith.tailo.feed.feedImage.dto.response.MemberFeedImageResponse;
import com.growith.tailo.feed.feedImage.service.FeedPostImageService;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedImageController {

    private final FeedPostImageService feedPostImageService;

    @Operation(
            summary = "특정 사용자 피드 이미지 목록 조회",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 사용자 피드 이미지 목록 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @GetMapping("/api/feed/{accountId}/images")
    public ResponseEntity<ApiResponse<MemberFeedImageListResponse>> getMemberFeedImageList(
            @PathVariable("accountId") Long accountId,
            Pageable pageable,
            @AuthenticationPrincipal Member member
    ) {

        Page<MemberFeedImageResponse> result = feedPostImageService.getMemberFeedImageList(member, pageable, accountId);

        MemberFeedImageListResponse response = new MemberFeedImageListResponse(result.getContent(), new Pagination(
                result.getNumber(),
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(response));
    }

}
