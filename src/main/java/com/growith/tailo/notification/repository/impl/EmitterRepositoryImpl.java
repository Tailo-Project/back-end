package com.growith.tailo.notification.repository.impl;

import com.growith.tailo.notification.repository.EmitterRepository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NotificationRepositoryImpl implements EmitterRepository {

    // 사용자별로 생성된 SseEmitter 객체를 관리
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    // 사용자에게 전송되지 못한 이벤트를 캐시로 관리
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByAccountId(String accountId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(accountId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByAccountId(String accountId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(accountId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void deleteAllEmitterStartWithId(String memberId) {
        emitters.keySet().removeIf(key -> key.startsWith(memberId));
    }

    @Override
    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.keySet().removeIf(key -> key.startsWith(memberId));
    }
}
