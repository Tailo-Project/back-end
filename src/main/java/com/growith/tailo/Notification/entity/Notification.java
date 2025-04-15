package com.growith.tailo.Notification.entity;

import com.growith.tailo.Notification.enums.NotificationType;
import com.growith.tailo.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

		@Enumerated(EnumType.STRING)
    private NotificationType type;

    private LocalDateTime createdAt;
    
    // getters and setters
}
