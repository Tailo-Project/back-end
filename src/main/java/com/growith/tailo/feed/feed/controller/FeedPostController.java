package com.growith.tailo.feed.feed.controller;

import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.feed.feed.dto.request.FeedPostRequest;
import com.growith.tailo.feed.feed.dto.request.FeedUpdateRequest;
import com.growith.tailo.feed.feed.service.FeedPostService;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestPart @Valid FeedPostRequest feedPostRequest,
            @RequestPart(required = false) List<MultipartFile> images,
            @AuthenticationPrincipal Member member) {

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
    @PatchMapping(value = "/{feedNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateFeedPost(
            @PathVariable("feedNumber") Long feedNumber,
            @RequestPart @Valid FeedUpdateRequest feedUpdateRequest,
            @RequestPart(required = false) List<MultipartFile> images,
            @AuthenticationPrincipal Member member) {

        String result = feedPostService.updateFeedPost(feedNumber, feedUpdateRequest, member, images);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(result));
    }
}
