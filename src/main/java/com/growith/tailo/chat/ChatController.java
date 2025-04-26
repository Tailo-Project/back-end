package com.growith.tailo.chat;


import com.growith.tailo.chat.member.ChatMember;
import com.growith.tailo.chat.message.ChatDTO;
import com.growith.tailo.chat.message.ChatPublisher;
import com.growith.tailo.chat.room.ChatRoomService;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatPublisher chatPublisher;
    private final ChatRoomService chatRoomService;
    private final MemberRepository memberRepository;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatDTO chatMessage) {
        Member member = memberRepository.findByAccountId(chatMessage.sender()).orElseThrow(()->new ResourceNotFoundException("해당 회원이 존재하지 않습니다."));
        chatRoomService.getChatRoom(member,1L);
        chatPublisher.send(chatMessage);
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public void addUser(@Payload ChatDTO chatMessage, SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//
//    }
}