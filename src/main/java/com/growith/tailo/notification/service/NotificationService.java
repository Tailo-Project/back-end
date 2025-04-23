package com.growith.tailo.notification.service;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.notification.enums.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(String accountId, String lastEventId);

    void send(Member receiver, Member sender, NotificationType type, String url);
}
