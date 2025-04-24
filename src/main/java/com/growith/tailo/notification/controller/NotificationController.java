package com.growith.tailo.notification.controller;

import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.notification.dto.NotificationDto;
import com.growith.tailo.notification.dto.NotificationListResponse;
import com.growith.tailo.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SEE 알림", description = "댓글, 좋아요, 챗팅 관련 알림 API")
@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // SSE 구독 요청
    @Operation(
            summary = "SSE 알림 구독",
            description = """
                    로그인한 사용자가 SSE 방식으로 실시간 알림을 수신합니다.
                    연결이 끊긴 경우 Last-Event-ID를 통해 마지막으로 받은 이벤트 ID 이후의 알림만 다시 받습니다.
                    연결 지속 시간은 1시간이며, 응답 타입은 text/event-stream 입니다.
                    """,
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
            })
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
            @AuthenticationPrincipal Member member,
            HttpServletResponse response) {

        return notificationService.subscribe(member.getAccountId(), lastEventId, response);

    }

    // 알림 목록 조회
    @Operation(
            summary = "알림 목록 조회",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "알림 목록 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<NotificationListResponse>> getNotifications(
            @AuthenticationPrincipal Member member,
            Pageable pageable) {

        Page<NotificationDto> notificationList = notificationService.getNotifications(member, pageable);

        NotificationListResponse result = new NotificationListResponse(notificationList.getContent(), new Pagination(
                notificationList.getNumber(),
                notificationList.getSize(),
                notificationList.getTotalPages(),
                notificationList.getTotalElements()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success("알림 목록 조회 성공", result));

    }

    // 알림 읽기 처리
    @Operation(
            summary = "알림 읽기 처리",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "알림 목록 조회 성공"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 읽음 처리 했거나 없는 알림"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
            })
    @PatchMapping("/{NotificationId}/read")
    public ResponseEntity<ApiResponse<String>> readNotification(
            @PathVariable("NotificationId") Long NotificationId,
            @AuthenticationPrincipal Member member) {
        String result = notificationService.markNotification(NotificationId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(result));
    }

    // 알림 test url
    //    @GetMapping("/send-test-notification")
    //    public ResponseEntity<String> sendTestNotification(@AuthenticationPrincipal Member member) {
    //        notificationService.send(member, member, NotificationType.LIKE, "http/localhost:8080/api/test");
    //        return ResponseEntity.ok("보냈음");
    //    }
}
