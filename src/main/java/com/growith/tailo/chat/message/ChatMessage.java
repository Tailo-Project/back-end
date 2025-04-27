package com.growith.tailo.chat.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growith.tailo.chat.room.ChatRoom;
//import com.growith.tailo.chat.member.ChatMember;
import com.growith.tailo.common.entity.BaseTime;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "chat_messages")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 기본적인 Lazy 로딩
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY) // 기본적인 Lazy 로딩
    @JoinColumn
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String content;

}
