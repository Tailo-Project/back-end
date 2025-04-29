package com.growith.tailo.chat;


//import com.growith.tailo.chat.member.ChatMember;
import com.growith.tailo.chat.message.ChatDTO;
import com.growith.tailo.chat.message.ChatPublisher;
import com.growith.tailo.chat.room.ChatRoomService;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatPublisher chatPublisher;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatDTO chatMessage) {
        log.info("들어온 메시지 : {} ", chatMessage);
        chatPublisher.send(chatMessage);
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public void addUser(@Payload ChatDTO chatMessage, SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//
//    }
}