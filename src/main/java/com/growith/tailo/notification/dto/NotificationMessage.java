package com.growith.tailo.notification.dto;

import com.growith.tailo.notification.enums.NotificationType;
import lombok.Builder;

@Builder
public record NotificationMessage(
        Long receiverId,
        Long senderId,
        NotificationType type,
        String url
) {
}
