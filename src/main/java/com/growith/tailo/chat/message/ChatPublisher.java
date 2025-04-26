package com.growith.tailo.chat.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;

    public void send(ChatDTO message){
        rabbitTemplate.convertAndSend(exchangeName, "chat.room."+message.roomId(),message);
    }
}
