package com.growith.tailo.chat.room.entity;

import com.growith.tailo.chat.message.entity.ChatMessage;
import com.growith.tailo.chat.roommemeber.ChatRoomMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.ALL)
    private List<ChatRoomMember> members;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages;
    
    // getters and setters
}
