package com.growith.tailo.auth.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growith.tailo.auth.jwt.service.CustomUserDetails;
import com.growith.tailo.common.dto.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("[403_FORBIDDEN] JWT Access Denied, 권한이 부족한 사용자 요청 차단:");
        log.warn("[403_FORBIDDEN] JWT Access Denied, Email={}, Role={}, Method={}, URI={}",
                getCurrentUserEmail(), getCurrentUserRole(), request.getMethod(), request.getRequestURI());
        log.warn("[403_FORBIDDEN] JWT Access Denied, ERROR_MESSAGE: {}", accessDeniedException.getMessage());
        CommonResponse<?> errorResponse = CommonResponse.error(403, "해당 리소스에 대한 접근 권한이 없습니다.");
        String json = new ObjectMapper().writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof CustomUserDetails userDetails ? userDetails.getEmail() : "Anonymous";
    }

    private String getCurrentUserRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof CustomUserDetails userDetails ? userDetails.getRole() : "UNKNOWN";
    }
}
