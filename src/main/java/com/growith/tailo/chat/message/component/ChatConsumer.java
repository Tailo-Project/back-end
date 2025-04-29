package com.growith.tailo.chat.message.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growith.tailo.chat.member.entity.ChatMember;
import com.growith.tailo.chat.member.repository.ChatMemberRepository;
import com.growith.tailo.chat.message.dto.ChatMessageRequest;
import com.growith.tailo.chat.message.dto.ChatMessageResponse;
import com.growith.tailo.chat.message.entity.ChatMessage;
import com.growith.tailo.chat.message.repository.ChatMessageRepository;
import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.chat.room.repository.ChatRoomRepository;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final ConnectionFactory connectionFactory;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;

    private SimpleMessageListenerContainer container;

    @Value("${spring.rabbitmq.chat.queue}")
    private String queuePrefix; // ex) "chat.room."

    public static final String DESTINATION = "/topic/chat/room/";

    public void startListening(String queueName) {
        if (container == null) {
            container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.setMessageListener(message -> {
                try {
                    String payload = new String(message.getBody(), StandardCharsets.UTF_8);
                    String queue = message.getMessageProperties().getConsumerQueue();
                    String roomId = extractRoomId(queue);

                    // 여기서 payload를 직접 JSON 파싱해서 ChatDTO로 변환
                    ChatMessageRequest chatMessageRequest = parsePayload(payload);

                    ChatMessage chatMessage = saveChatMessage(chatMessageRequest);

                    ChatMessageResponse chatMessageResponse = ChatMessageResponse.fromEntity(chatMessage);

                    messagingTemplate.convertAndSend(DESTINATION + roomId, chatMessageResponse);
                } catch (Exception e) {
                    log.error("메시지 처리 실패", e);
                }
            });
        }
        container.addQueueNames(queueName);
        if (!container.isRunning()) {
            container.start();
        }
    }

    public void stopListening(String queueName) {
        if (container != null) {
            container.removeQueueNames(queueName);
            if (container.getQueueNames().length == 0) {
                container.stop();
                container = null;
            }
        }
    }

    private String extractRoomId(String queueName) {
        return queueName.replace(queuePrefix, ""); // "chat.room.123" -> "123"
    }

    private ChatMessageRequest parsePayload(String payload) {
        // Jackson으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(payload, ChatMessageRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("메시지 파싱 실패", e);
        }
    }

    @Transactional
    public ChatMessage saveChatMessage(ChatMessageRequest message) {
        ChatRoom chatRoom = chatRoomRepository.findById(message.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("채팅방을 찾을 수 없습니다. id=" + message.roomId()));

        Member chatMember = memberRepository.findByAccountId(message.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다. accountId=" + message.accountId()));

        ChatMember sender = chatMemberRepository.findByMemberAndChatRoom(chatMember,chatRoom)
                .orElseThrow(() -> new ResourceNotFoundException("채팅 멤버를 찾을 수 없습니다. accountId=" + message.accountId()));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(message.content())
                .sender(sender)
                .build();

        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }
}
