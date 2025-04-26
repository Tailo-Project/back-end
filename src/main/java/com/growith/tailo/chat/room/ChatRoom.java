package com.growith.tailo.chat.room;

import com.growith.tailo.chat.member.ChatMember;
import com.growith.tailo.chat.message.ChatMessage;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "chat_rooms")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @OneToMany(mappedBy = "chatRoom",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMember> chatMember;

    @OneToMany(mappedBy = "chatRoom",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

}
