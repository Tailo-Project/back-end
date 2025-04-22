package com.growith.tailo.chat.message.entity;

import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.common.entity.BaseTime;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_messages")
public class ChatMessage extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "chat_rooms_id")
    private ChatRoom chatRoom;

    private String message;
    
}