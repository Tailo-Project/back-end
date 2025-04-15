package com.growith.tailo.chat.entity;

import com.growith.tailo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_a_id")
    private User userA;

    @ManyToOne
    @JoinColumn(name = "user_b_id")
    private User userB;
    
    // getters and setters
}
