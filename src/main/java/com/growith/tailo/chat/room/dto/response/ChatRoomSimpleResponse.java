package com.growith.tailo.chat.room.dto.response;

import com.growith.tailo.chat.room.entity.ChatRoom;
import lombok.Builder;

@Builder
public record ChatRoomSimpleResponse (Long roomId, String roomName, int memberCount){
    public static ChatRoomSimpleResponse fromEntity(ChatRoom chatRoom){
        return ChatRoomSimpleResponse.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .memberCount(chatRoom.getChatMembers().size())
                .build();
    }
}
