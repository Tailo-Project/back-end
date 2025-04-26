package com.growith.tailo.chat;


import com.growith.tailo.chat.message.ChatDTO;
import com.growith.tailo.chat.message.ChatPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private ChatPublisher chatPublisher;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatDTO chatMessage) {
        chatPublisher.send(chatMessage);
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public void addUser(@Payload ChatDTO chatMessage, SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//
//    }
}