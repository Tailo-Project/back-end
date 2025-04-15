package com.growith.tailo.auth.oauth.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.growith.tailo.common.dto.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("[401_UNAUTHORIZED] OAuth2 Unauthorized Access, 인증되지 않은 사용자 요청 차단:");
        log.warn("[401_UNAUTHORIZED] OAuth2 Unauthorized Access, Method={}, URI={}, IP={}",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        log.warn("[401_UNAUTHORIZED] OAuth2 Unauthorized Access, ERROR_MESSAGE: {}", exception.getMessage());

        String message = exception instanceof BadCredentialsException
                ? "비밀번호가 일치하지 않습니다." : "로그인 실패: 잘못된 사용자 이름 또는 비밀번호입니다.";
        CommonResponse<?> errorResponse = CommonResponse.error(401, message);
        String json = new ObjectMapper().writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}
