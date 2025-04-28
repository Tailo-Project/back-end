package com.growith.tailo.chat.room;

import com.growith.tailo.member.entity.Member;
import lombok.Builder;

@Builder
public record ChatRoomResponse(Long roomId, String accountId1, String accountId2, String roomName) {
    public static ChatRoomResponse fromEntity(ChatRoom chatRoom){
        return  ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .accountId1(chatRoom.getChatMember1().getAccountId())
                .accountId2(chatRoom.getChatMember2().getAccountId())
                .roomName(chatRoom.getRoomName())
                .build();
    }
}
