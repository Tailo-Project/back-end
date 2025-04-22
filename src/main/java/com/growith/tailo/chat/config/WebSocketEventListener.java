package com.growith.tailo.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketEventListener {

    // 웹소켓 연결된 사용자 세션 정보를 관리할 Map
    private final Map<String, String> userSessions = new ConcurrentHashMap<>();

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = getUsernameFromEvent(event); // 이벤트에서 사용자 이름 또는 ID 추출 (예시)

        // 세션 연결 시 사용자 정보를 저장
        userSessions.put(sessionId, username);

        log.info("웹소켓 연결됨: sessionId=" + sessionId + ", username=" + username);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();

        // 세션 해제 시 사용자 정보 제거
        String username = userSessions.remove(sessionId);

        log.info("웹소켓 연결 해제: sessionId=" + sessionId + ", username=" + username);
    }

    // 예시: 이벤트에서 사용자 정보(예: 사용자 이름)를 추출하는 방법
    private String getUsernameFromEvent(SessionConnectEvent event) {
            // Spring Security 인증 정보를 가져오는 방법
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();  // 사용자 이름을 반환
            }
            return "anonymous";  // 인증되지 않은 경우
        }
}
