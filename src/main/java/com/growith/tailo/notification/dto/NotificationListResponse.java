package com.growith.tailo.notification.dto;

import com.growith.tailo.common.dto.Pagination;
import lombok.Builder;

import java.util.List;

@Builder
public record NotificationListResponse(
        List<NotificationDto> notificationList,
        Pagination pagination
) {

}
