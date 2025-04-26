package com.growith.tailo.chat.message;

public record ChatDTO(MessageType type, String content, Long accountId, Long roomId) {

}