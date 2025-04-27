package com.growith.tailo.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String eventCacheId, Object event);

    Map<String, SseEmitter> findAllEmitterStartWithByAccountId(String accountId);

    Map<String, Object> findAllEventCacheStartWithByAccountId(String accountId);

    void deleteById(String id);

    void deleteAllEmitterStartWithId(String memberId);

    void deleteAllEventCacheStartWithId(String memberId);
}
