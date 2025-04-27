package com.growith.tailo.feed.likes.controller;

import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.feed.likes.service.PostLikeService;

import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(
            summary = "좋아요",
            description = "이미 좋아요를 한 사용자면 좋아요 취소",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "좋아요 성공/실패"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @PostMapping("/{feedId}/likes")
    public ResponseEntity<ApiResponse<String>> likeFeedPost(
            @PathVariable("feedId") Long feedId,
            @AuthenticationPrincipal Member member) {

        String result = postLikeService.likeFeedPost(feedId, member);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(result));

    }

    @Operation(
            summary = "좋아요 수 조회",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "좋아요 수 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @GetMapping("/{feedId}/likes")
    public ResponseEntity<ApiResponse<String>> countLikes(
            @PathVariable("feedId") Long feedId,
            @AuthenticationPrincipal Member member) {

        String result = postLikeService.countLikes(feedId, member);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(result));

    }
}
