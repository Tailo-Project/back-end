package com.growith.tailo.chat.message.dto;

public record ChatMessageRequest(Long roomId, String accountId, String messageType, String content) {

    @Override
    public String toString() {
        return "ChatMessageRequest{" +
                "roomId=" + roomId +
                ", sender='" + accountId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}