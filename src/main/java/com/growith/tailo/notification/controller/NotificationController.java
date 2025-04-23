package com.growith.tailo.notification.controller;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // SSE 구독 요청
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
            @AuthenticationPrincipal Member member) {

        return notificationService.subscribe(member.getAccountId(), lastEventId);

    }

    // 알림 test url
    //    @GetMapping("/send-test-notification")
    //    public ResponseEntity<String> sendTestNotification(@AuthenticationPrincipal Member member) {
    //        notificationService.send(member, member, NotificationType.LIKE, "http/localhost:8080/api/test");
    //        return ResponseEntity.ok("보냈음");
    //    }
}
