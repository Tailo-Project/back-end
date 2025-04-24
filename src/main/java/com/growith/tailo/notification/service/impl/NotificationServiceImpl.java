package com.growith.tailo.notification.service.impl;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.notification.dto.NotificationDto;
import com.growith.tailo.notification.entity.Notification;
import com.growith.tailo.notification.enums.NotificationType;
import com.growith.tailo.notification.repository.EmitterRepository;
import com.growith.tailo.notification.repository.NotificationRepository;
import com.growith.tailo.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    // SSE 연결 지속 시간 - 1시간
    private final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    // subscribe
    @Override
    public SseEmitter subscribe(String accountId, String lastEventId, HttpServletResponse response) {
        String emitterId = makeTimeIncludeId(accountId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 더미 이벤트 발송
        String eventId = makeTimeIncludeId(accountId);
        sendNotification(emitter, eventId, emitterId, "[accountId]: " + accountId + " connected.");

        // 클라이언트가 미수신한 Event 목록이 있을 경우 전송
        if (!lastEventId.isEmpty()) {
            sendLostData(lastEventId, accountId, emitterId, emitter);
        }

        // 실시간 응답이 필요하기 때문에 Nginx 버퍼링 옵션 off
        response.setHeader("X-Accel-Buffering", "no");

        return emitter;
    }

    private String makeTimeIncludeId(String accountId) {
        return accountId + "_" + System.currentTimeMillis();
    }

    private void sendLostData(String lastEventId, String accountId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByAccountId(accountId);

        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));

        emitterRepository.deleteAllEventCacheStartWithId(accountId);
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    // send
    @Override
    public void send(Member receiver, Member sender, NotificationType type, String url) {
        Notification notification = Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .type(type)
                .url(url)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        String receiverAccountId = receiver.getAccountId();
        String eventId = makeTimeIncludeId(receiverAccountId);
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByAccountId(receiverAccountId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationDto.of(notification));
                }
        );
    }

    // 알림 목록 조회
    @Override
    public Page<NotificationDto> getNotifications(Member member, Pageable pageable) {

        // TODO : 읽음 처리되지 않은 것만 할 지, 읽음 처리된 것도 볼 수 있도록 할 지 정하기!
        Page<Notification> notificationList = notificationRepository.findAllByReceiver(member, pageable);

        return notificationList.map(notification -> NotificationDto.of(notification));
    }

    // 알림 읽음 처리
    @Override
    public String MarkNotification(Long NotificationId) {
        boolean isUpdated = notificationRepository.markNotification(NotificationId) > 0;

        if (!isUpdated) {
            throw new ResourceNotFoundException("해당 알림이 존재하지 않거나 이미 처리되었습니다.");
        }

        String result = String.format("%d 알림 읽음 처리 성공", NotificationId);

        return result;
    }


}
