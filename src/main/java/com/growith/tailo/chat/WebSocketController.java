package com.growith.tailo.chat;

import com.growith.tailo.chat.message.dto.ChatMessageRequest;
import com.growith.tailo.chat.message.component.ChatPublisher;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChatPublisher chatPublisher;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessageRequest chatMessage) {
        chatPublisher.send(chatMessage);
    }

}