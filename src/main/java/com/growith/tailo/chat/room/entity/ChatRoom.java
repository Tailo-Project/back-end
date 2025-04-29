package com.growith.tailo.chat.room.entity;

//import com.growith.tailo.chat.member.entity.ChatMember;
import com.growith.tailo.chat.member.entity.ChatMember;
import com.growith.tailo.chat.message.entity.ChatMessage;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "chat_rooms")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @OneToMany(mappedBy = "chatRoom",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMember> chatMembers= new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public void addChatMember(ChatMember chatMember) {
        chatMembers.add(chatMember);
        chatMember.setChatRoom(this);  // 양방향 연관 관계 설정
    }

}
