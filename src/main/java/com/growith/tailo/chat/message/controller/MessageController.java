package com.growith.tailo.chat.message.controller;

import com.growith.tailo.chat.message.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    // 채팅 메시지 처리 메서드
    @MessageMapping("/chat/message")
    @SendTo("/pub/public") // 모든 사용자에게 메시지를 전송
    public ChatMessageDto sendMessage(ChatMessageDto message) {
        if (message.getMessage().contains("에러")) {
            throw new IllegalArgumentException("잘못된 메시지입니다.");
        }

        // 특정 채팅방으로 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        return message;
    }

    // 예외 처리 메서드
    @MessageExceptionHandler
    @SendToUser("/queue/errors") // 오류 메시지를 해당 사용자에게 전송
    public String handleException(Throwable exception) {
        return "에러 발생: " + exception.getMessage();
    }
}