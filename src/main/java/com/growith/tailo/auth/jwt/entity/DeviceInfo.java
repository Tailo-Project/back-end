package com.growith.tailo.auth.jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceInfo {
    @Column(nullable = false)
    private String deviceId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType deviceType;
    private String deviceName;
    private String ipAddress;
    private String userAgent;

    public enum DeviceType {
        WEB,
        ANDROID,
        IOS,
        DESKTOP
    }
}
