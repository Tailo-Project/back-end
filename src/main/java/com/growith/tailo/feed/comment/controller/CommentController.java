package com.growith.tailo.feed.comment.controller;

import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.feed.comment.dto.CommentRequest;
import com.growith.tailo.feed.comment.service.CommentService;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@Tag(name = "Feed Comment Post", description = "피드 댓글 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 등록",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "댓글 등록 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @PostMapping("/{feedId}/comments")
    public ResponseEntity<ApiResponse<String>> registerComment(
            @PathVariable("feedId") Long feedId,
            @RequestBody @Valid CommentRequest commentRequest,
            @AuthenticationPrincipal Member member
    ) {

        String result = commentService.registerComment(feedId, commentRequest, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponses.created(result));
    }

}
