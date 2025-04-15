package com.growith.tailo.auth.jwt.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.growith.tailo.common.dto.CommonResponse;
import com.nimbusds.jose.util.StandardCharset;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("[401_UNAUTHORIZED] JWT Unauthorized Access, 인증되지 않은 사용자 요청 차단:");
        log.warn("[401_UNAUTHORIZED] JWT Unauthorized Access, Method={}, URI={}, IP={}",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        log.warn("[401_UNAUTHORIZED] JWT Unauthorized Access, ERROR_MESSAGE: {}", authException.getMessage());
        CommonResponse<?> errorResponse = CommonResponse.error(401, "로그인이 필요합니다.");
        String json = new ObjectMapper().writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharset.UTF_8.name());
        response.getWriter().write(json);
    }
}
