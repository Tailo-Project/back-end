package com.growith.tailo.feed.feed.controller;

import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.feed.feed.dto.request.FeedPostRequest;
import com.growith.tailo.feed.feed.dto.request.FeedUpdateRequest;
import com.growith.tailo.feed.feed.dto.response.FeedPostListResponse;
import com.growith.tailo.feed.feed.dto.response.FeedPostResponse;
import com.growith.tailo.feed.feed.service.FeedPostService;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Feed Post", description = "피드 포스트 API")
@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedPostController {

    private final FeedPostService feedPostService;

    @Operation(
            summary = "피드 등록",
            description = "이미지와 JSON 데이터를 함께 전송",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "피드 작성 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> registerFeedPost(
            @RequestPart("feedPostRequest") @Valid FeedPostRequest feedPostRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal Member member) {

        if (images == null) {
            images = new ArrayList<>();
        }

        String result = feedPostService.registerFeedPost(feedPostRequest, member, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponses.created(result));
    }

    @Operation(
            summary = "피드 수정",
            description = "이미지와 JSON 데이터를 함께 전송",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "피드 작성 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @PatchMapping(value = "/{feedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateFeedPost(
            @PathVariable("feedId") Long feedId,
            @RequestPart("feedUpdateRequest") @Valid FeedUpdateRequest feedUpdateRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal Member member) {

        if (images == null) {
            images = new ArrayList<>();
        }

        String result = feedPostService.updateFeedPost(feedId, feedUpdateRequest, member, images);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(result));
    }

    @Operation(
            summary = "나와 나의 팔로우 피드 조회",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "피드 목록 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<FeedPostListResponse>> getFeedPostList(
            Pageable pageable,
            @AuthenticationPrincipal Member member) {

        Page<FeedPostResponse> feedPostList = feedPostService.getFeedPostList(member, pageable);

        FeedPostListResponse response = new FeedPostListResponse(feedPostList.getContent(), new Pagination(
                feedPostList.getNumber(),
                feedPostList.getSize(),
                feedPostList.getTotalPages(),
                feedPostList.getTotalElements()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(response));

    }

    @Operation(
            summary = "특정 피드 조회",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "피드 목록 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @GetMapping("/feedId")
    public ResponseEntity<ApiResponse<FeedPostResponse>> getFeedPost(
            @PathVariable("feedId") Long feedId,
            @AuthenticationPrincipal Member member) {

        FeedPostResponse result = feedPostService.getFeedPost(feedId, member);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(result));

    }

    @Operation(

            summary = "피드 삭제",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "피드 목록 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })

    @DeleteMapping("/{feedId}")
    public ResponseEntity<ApiResponse<String>> deleteFeedPost(
            @PathVariable("feedId") Long feedId,
            @AuthenticationPrincipal Member member) {

        String result = feedPostService.deleteFeedPost(feedId, member);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(result));

    }


}
