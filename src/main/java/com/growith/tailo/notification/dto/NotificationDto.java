package com.growith.tailo.notification.dto;

import com.growith.tailo.notification.entity.Notification;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationDto(
        Long id,
        String message,
        String url,
        boolean isRead,
        LocalDateTime createdAt
) {

    public static NotificationDto of(Notification notification) {
        String senderName = notification.getSender().getAccountId();
        String message = switch (notification.getType()) {
            case LIKE -> senderName + "님이 피드에 좋아요를 눌렀습니다.";
            case COMMENT -> senderName + "님이 댓글을 남겼습니다.";
            case DM -> senderName + "님이 메시지를 보냈습니다.";
        };

        return NotificationDto.builder()
                .id(notification.getId())
                .message(message)
                .url(notification.getUrl())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
