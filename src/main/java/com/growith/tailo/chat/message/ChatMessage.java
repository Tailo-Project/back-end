package com.growith.tailo.chat.message;

import com.growith.tailo.chat.room.ChatRoom;
import com.growith.tailo.chat.member.ChatMember;
import com.growith.tailo.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_messages")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private ChatMember sender;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String content;
}
