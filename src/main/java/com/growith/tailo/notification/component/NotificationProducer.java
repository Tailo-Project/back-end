package com.growith.tailo.notification.component;

import com.growith.tailo.notification.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.notification.exchange}")
    private String notificationExchange;

    @Value("${spring.rabbitmq.notification.routing-key}")
    private String notificationRouting;

    public void sendNotification(NotificationMessage message) {
        rabbitTemplate.convertAndSend(notificationExchange, notificationRouting, message);
    }
}
