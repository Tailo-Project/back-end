package com.growith.tailo.notification.component;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import com.growith.tailo.notification.dto.NotificationMessage;
import com.growith.tailo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final MemberRepository memberRepository;

    @RabbitListener(queues = "${spring.rabbitmq.notification.queue}")
    public void handleNotification(NotificationMessage message) {
        try {

            Member receiver = memberRepository.findById(message.receiverId())
                    .orElseThrow(() -> new ResourceNotFoundException("받는 회원이 존재하지 않습니다."));
            Member sender = memberRepository.findById(message.receiverId())
                    .orElseThrow(() -> new ResourceNotFoundException("보내는 회원이 존재하지 않습니다."));

            notificationService.send(receiver, sender, message.type(), message.url());
        } catch (Exception e) {
            log.error("Failed to process like notification: {}", message, e);
        }
    }
}
