package com.growith.tailo.feed.comment.controller;

import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.feed.comment.dto.request.CommentRequest;
import com.growith.tailo.feed.comment.dto.response.CommentListResponse;
import com.growith.tailo.feed.comment.dto.response.CommentResponse;
import com.growith.tailo.feed.comment.dto.response.ReplyResponse;
import com.growith.tailo.feed.comment.dto.response.ReplyTotalListResponse;
import com.growith.tailo.feed.comment.service.CommentService;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Operation(
            summary = "댓글 목록 조회",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "댓글 목록 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @GetMapping("/{feedId}/comments")
    public ResponseEntity<ApiResponse<CommentListResponse>> getCommentList(
            @PathVariable("feedId") Long feedId,
            @AuthenticationPrincipal Member member,
            Pageable pageable
    ) {

        Page<CommentResponse> commentList = commentService.getCommentList(feedId, member, pageable);

        CommentListResponse result = new CommentListResponse(commentList.getContent(), new Pagination(
                commentList.getNumber(),
                commentList.getSize(),
                commentList.getTotalPages(),
                commentList.getTotalElements()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.created("댓글 목록 조회 성공", result));
    }

    @Operation(
            summary = "대댓글 목록 조회 (더보기)",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "대댓글 목록 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @GetMapping("/{feedId}/comments/{commentId}/replies")
    public ResponseEntity<ApiResponse<ReplyTotalListResponse>> getReplyList(
            @PathVariable("feedId") Long feedId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal Member member,
            Pageable pageable
    ) {

        Page<ReplyResponse> replyList = commentService.getReplyList(feedId, commentId, member, pageable);

        ReplyTotalListResponse result = new ReplyTotalListResponse(replyList.getContent(), new Pagination(
                replyList.getNumber(),
                replyList.getSize(),
                replyList.getTotalPages(),
                replyList.getTotalElements()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.created("대댓글 목록 조회 성공", result));
    }


    @Operation(
            summary = "댓글 삭제",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
            })
    @DeleteMapping("/{feedId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(
            @PathVariable("feedId") Long feedId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal Member member
    ) {

        String result = commentService.deleteComment(feedId, commentId, member);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(result));
    }

}
