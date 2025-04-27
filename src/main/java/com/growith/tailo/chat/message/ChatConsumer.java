package com.growith.tailo.chat.message;


import com.growith.tailo.chat.room.ChatRoom;
import com.growith.tailo.chat.room.ChatRoomRepository;
import com.growith.tailo.common.config.RabbitMQConfig;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatConsumer {
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public static final String DESTINATION="/topic/chat/room/";

    @Transactional
    @RabbitListener(queues = "${spring.rabbitmq.chat-queue}" )
    public void receiveChatMessage(ChatDTO message){
        ChatRoom chatRoom = chatRoomRepository.findById(message.roomId()).orElseThrow(()->new ResourceNotFoundException(""));
        Member chatMember = memberRepository.findByAccountId(message.sender()).orElseThrow(()->new ResourceNotFoundException(""));
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(message.content())
                .sender(chatMember)
                .build();
        chatMessageRepository.save(chatMessage);
        messagingTemplate.convertAndSend(DESTINATION+chatRoom.getId(),message);
    }


//    추후 도입 동적 관리
//    private final ConnectionFactory connectionFactory;
//    private final SimpMessagingTemplate messagingTemplate;
//
//    // roomId 기준으로 ListenerContainer를 Map으로 관리
//    private final Map<Long, SimpleMessageListenerContainer> containerMap = new HashMap<>();
//
//    public void startListening(Long roomId) {
//        // 이미 리스닝 중이면 pass
//        if (containerMap.containsKey(roomId)) return;
//
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames("chat.room." + roomId);
//        container.setMessageListener(message -> {
//            try {
//                String payload = new String(message.getBody());
//                messagingTemplate.convertAndSend("/topic/chat.room." + roomId, payload);
//            } catch (Exception e) {
//                log.error("Message processing failed for roomId: " + roomId, e);
//            }
//        });
//
//        container.start();
//        containerMap.put(roomId, container);
//    }
//
//    public void stopListening(Long roomId) {
//        if (containerMap.containsKey(roomId)) {
//            containerMap.get(roomId).stop();
//            containerMap.remove(roomId);
//        }
//    }
}