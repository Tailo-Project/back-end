package com.growith.tailo.notification.enums;

public enum NotificationType {
    LIKE("좋아요"), COMMENT("댓글"), DM("메시지");

    private final String notificationType;

    NotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}