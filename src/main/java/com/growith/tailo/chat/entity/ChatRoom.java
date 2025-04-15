package com.growith.tailo.chat.entity;

import com.growith.tailo.member.entity.Member;
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
    private Member userA;

    @ManyToOne
    @JoinColumn(name = "user_b_id")
    private Member userB;
    
    // getters and setters
}
