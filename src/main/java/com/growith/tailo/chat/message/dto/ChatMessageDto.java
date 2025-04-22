package com.growith.tailo.chat.message.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long roomId;      // 어떤 방인지
    private String sender;    // 보낸 사람 이름 or ID
    private String message;   // 메시지 내용
    private LocalDateTime createdAt;
}