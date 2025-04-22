package com.growith.tailo.chat.roommemeber;

import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.common.entity.BaseTime;
import com.growith.tailo.member.entity.Member;
import jakarta.persistence.*;

@Entity
public class ChatRoomMember extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private boolean deleted = false;

    // 삭제 업데이트 - 업데이트 시간 변경되면서 생성 시간과 달라 짐 - 재 입장 시점에 검증
}