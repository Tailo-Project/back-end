package com.growith.tailo.notification.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationListResponse(
        List<NotificationDto> notificationList
) {

}
