package com.growith.tailo.chat.member;


import com.growith.tailo.chat.room.ChatRoom;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_members")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean isDelete; // 채팅방에 포함 된 인원 전부 삭제 시 채팅방 데이터 삭제


}
