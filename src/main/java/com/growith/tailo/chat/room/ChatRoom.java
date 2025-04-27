package com.growith.tailo.chat.room;

//import com.growith.tailo.chat.member.ChatMember;
import com.growith.tailo.chat.message.ChatMessage;
import com.growith.tailo.member.entity.Member;
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
    private Long id;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @ManyToOne
    @JoinColumn(name="chat_member_1",nullable = false)
    private Member chatMember1;

    @ManyToOne
    @JoinColumn(name="chat_member_2",nullable = false)
    private Member chatMember2;


    @OneToMany(mappedBy = "chatRoom",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

}
