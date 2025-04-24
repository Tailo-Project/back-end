package com.growith.tailo.notification.entity;

import com.growith.tailo.common.entity.BaseTime;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.notification.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification extends BaseTime {

    @Id
    @Column(name = "notification_id")

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String url;

    @Column(nullable = false)
    private boolean isRead = false;

    @Builder
    public Notification(Member receiver, Member sender, NotificationType type, String url, Boolean isRead) {
        this.receiver = receiver;
        this.sender = sender;
        this.type = type;
        this.url = url;
        this.isRead = isRead;
    }

}
