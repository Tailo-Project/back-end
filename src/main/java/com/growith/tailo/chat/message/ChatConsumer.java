package com.growith.tailo.chat.message;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatConsumer {

    private final ConnectionFactory connectionFactory;
    private final SimpMessagingTemplate messagingTemplate;

    // roomId 기준으로 ListenerContainer를 Map으로 관리
    private final Map<Long, SimpleMessageListenerContainer> containerMap = new HashMap<>();

    public void startListening(Long roomId) {
        // 이미 리스닝 중이면 pass
        if (containerMap.containsKey(roomId)) return;

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("chat.room." + roomId);
        container.setMessageListener(message -> {
            try {
                String payload = new String(message.getBody());
                messagingTemplate.convertAndSend("/topic/chat.room." + roomId, payload);
            } catch (Exception e) {
                log.error("Message processing failed for roomId: " + roomId, e);
            }
        });

        container.start();
        containerMap.put(roomId, container);
    }

    public void stopListening(Long roomId) {
        if (containerMap.containsKey(roomId)) {
            containerMap.get(roomId).stop();
            containerMap.remove(roomId);
        }
    }
}