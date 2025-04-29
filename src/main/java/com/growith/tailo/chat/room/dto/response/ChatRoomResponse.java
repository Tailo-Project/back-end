package com.growith.tailo.chat.room.dto.response;

import com.growith.tailo.chat.member.dto.ChatMemberDto;
import com.growith.tailo.chat.room.entity.ChatRoom;
import lombok.Builder;

import java.util.List;

@Builder
public record ChatRoomResponse(Long roomId, String roomName, List<ChatMemberDto> members, int countMember ) {
    public static ChatRoomResponse fromEntity(ChatRoom chatRoom){
        return  ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .members(chatRoom.getChatMembers().stream().map(ChatMemberDto::fromEntity).toList())
                .roomName(chatRoom.getRoomName())
                .countMember(chatRoom.getChatMembers().size())
                .build();
    }
}
