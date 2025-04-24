package com.growith.tailo.notification.service;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.notification.dto.NotificationDto;
import com.growith.tailo.notification.enums.NotificationType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(String accountId, String lastEventId, HttpServletResponse response);

    void send(Member receiver, Member sender, NotificationType type, String url);

    Page<NotificationDto> getNotifications(Member member, Pageable pageable);

    String markNotification(Long NotificationId);
}
