package com.growith.tailo.chat.message.entity;

import com.growith.tailo.chat.member.entity.ChatMember;
import com.growith.tailo.chat.room.entity.ChatRoom;
//import com.growith.tailo.chat.member.entity.ChatMember;
import com.growith.tailo.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_messages")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 기본적인 Lazy 로딩
    @JoinColumn(name = "sender_id")
    private ChatMember sender;

    @ManyToOne(fetch = FetchType.LAZY) // 기본적인 Lazy 로딩
    @JoinColumn
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String content;

}
