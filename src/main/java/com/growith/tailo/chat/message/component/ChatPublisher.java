package com.growith.tailo.chat.message.component;

import com.growith.tailo.chat.message.dto.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.chat.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.chat.routing-key}")
    private String chatRoutingKey;

    public void send(ChatMessageRequest message){
        log.info("퍼블리셔: 메시지 {}",message.toString());
        rabbitTemplate.convertAndSend(exchangeName, chatRoutingKey+message.roomId(),message);
    }

}
