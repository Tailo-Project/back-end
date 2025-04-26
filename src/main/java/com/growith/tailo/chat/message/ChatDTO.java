package com.growith.tailo.chat.message;

import java.time.LocalDateTime;

public record ChatDTO(Long roomId, String sender, String messageType, String content) {

}