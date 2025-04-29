package com.growith.tailo.chat.message.dto;

import com.growith.tailo.chat.message.entity.ChatMessage;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponse(Long roomId, String accountId, String profileImageUrl, String content, LocalDateTime createdAt) {
    public static ChatMessageResponse fromEntity(ChatMessage chatMessage){
        return ChatMessageResponse.builder()
                .roomId(chatMessage.getChatRoom().getId())
                .accountId(chatMessage.getSender().getMember().getAccountId())
                .profileImageUrl(chatMessage.getSender().getMember().getProfileImageUrl())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
