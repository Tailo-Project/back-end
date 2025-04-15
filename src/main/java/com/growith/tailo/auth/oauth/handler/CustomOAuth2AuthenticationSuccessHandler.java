package com.growith.tailo.auth.oauth.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.growith.tailo.auth.jwt.JwtTokenProvider;
import com.growith.tailo.auth.jwt.dto.JwtTokenResponse;
import com.growith.tailo.auth.jwt.dto.JwtTokenUpdateRequest;
import com.growith.tailo.auth.jwt.entity.DeviceInfo;
import com.growith.tailo.auth.jwt.entity.JwtToken;
import com.growith.tailo.auth.jwt.repository.JwtTokenRepository;
import com.growith.tailo.auth.jwt.service.CustomUserDetails;
import com.growith.tailo.common.dto.CommonResponse;
import com.growith.tailo.user.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /* 로그인 성공 처리 로직 */
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        DeviceInfo deviceInfo = getDeviceInfo(request);
        String deviceId = deviceInfo.getDeviceId();
        User user = customUserDetails.getUser();

        /* JWT 토큰 생성 */
        String accessToken = jwtTokenProvider.generateAccessToken(customUserDetails.getEmail(), customUserDetails.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails.getEmail(), customUserDetails.getRole());
        Claims accessTokenClaims = jwtTokenProvider.parseClaims(accessToken);
        Claims refreshTokenClaims = jwtTokenProvider.parseClaims(refreshToken);

        // TODO: 토큰 정보 확인 후 존재하면 갱신, 존재하지 않으면 생성
        JwtToken jwtToken = jwtTokenRepository.findByUserAndDeviceInfo_DeviceId(user, deviceId)
                .map((token) -> token.update(JwtTokenUpdateRequest.builder()
                        .deviceInfo(deviceInfo)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .accessTokenIssuedAt(accessTokenClaims.getIssuedAt().toInstant())
                        .accessTokenExpiresAt(accessTokenClaims.getExpiration().toInstant())
                        .refreshTokenIssuedAt(refreshTokenClaims.getIssuedAt().toInstant())
                        .refreshTokenExpiresAt(refreshTokenClaims.getExpiration().toInstant())
                        .build()))
                .orElseGet(() -> JwtToken.builder()
                        .user(user)
                        .deviceInfo(deviceInfo)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .accessTokenIssuedAt(accessTokenClaims.getIssuedAt().toInstant())
                        .refreshTokenIssuedAt(refreshTokenClaims.getIssuedAt().toInstant())
                        .accessTokenExpiresAt(accessTokenClaims.getExpiration().toInstant())
                        .refreshTokenExpiresAt(refreshTokenClaims.getExpiration().toInstant())
                        .build());

        /* TODO: JWT 토큰 및 디바이스 정보 저장 */
        printLog(jwtToken, deviceInfo);
        jwtTokenRepository.save(jwtToken);
        CommonResponse<?> successResponse = CommonResponse.success(
                JwtTokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );

        // TODO: 추후 HttpOnly 쿠키 + CSRF 보완 필요
        // NOTE: OAuth2 Google 테스트 URL: http://localhost:8080/oauth2/authorization/google */
        String json = new ObjectMapper().writeValueAsString(successResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(json);
        // response.sendRedirect("/");
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private DeviceInfo getDeviceInfo(HttpServletRequest request) {
        return DeviceInfo.builder()
                .deviceId(getDeviceId(request))
                .deviceType(getDeviceType(request))
                .deviceName(getDeviceName(request))
                .ipAddress(getIpAddress(request))
                .userAgent(request.getHeader(HttpHeaders.USER_AGENT))
                .build();
    }

    private String getDeviceId(HttpServletRequest request) {
        String deviceId = request.getHeader("X-Device-ID");
        return deviceId != null ? deviceId : generateDeviceId(request);
    }

    private DeviceInfo.DeviceType getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT).toLowerCase();
        return !userAgent.contains("mobile") ? DeviceInfo.DeviceType.WEB :
                userAgent.contains("android") ? DeviceInfo.DeviceType.ANDROID :
                        DeviceInfo.DeviceType.IOS;
    }

    private String getDeviceName(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT).toLowerCase();
        String os = detectOS(userAgent);
        String device = detectDevice(userAgent);
        String browser = detectBrowser(userAgent);
        return String.format("%s%s%s", os,
                device.isEmpty() ? "" : " (" + device + ")",
                browser.isEmpty() ? "" : " · " + browser);
    }

    private String getIpAddress(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        return clientIp != null ? clientIp : request.getRemoteAddr();
    }

    private String generateDeviceId(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) ipAddress = request.getRemoteAddr();
        String raw = userAgent + "|" + ipAddress;
        return UUID.nameUUIDFromBytes(raw.getBytes(StandardCharsets.UTF_8)).toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String detectOS(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) return "Unknown OS";

        String ua = userAgent.toLowerCase();
        if (ua.contains("windows nt 11")) return "Windows 11";
        if (ua.contains("windows nt 10")) return "Windows 10";
        if (ua.contains("windows nt 6.1")) return "Windows 7";
        if (ua.contains("mac os x")) return "Mac OS";
        if (ua.contains("android")) return "Android";
        if (ua.contains("iphone")) return "iOS";
        if (ua.contains("ipad")) return "iPadOS";
        if (ua.contains("linux")) return "Linux";
        return "Unknown OS";
    }


    private String detectDevice(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) return "Unknown Device";

        String ua = userAgent.toLowerCase();
        if (ua.contains("samsung")) return "Samsung Galaxy";
        if (ua.contains("sm-")) return "Samsung Device";
        if (ua.contains("pixel")) return "Google Pixel";
        if (ua.contains("iphone")) return "iPhone";
        if (ua.contains("ipad")) return "iPad";
        if (ua.contains("macintosh")) return "Mac";
        if (ua.contains("windows")) return "PC";
        return "Unknown Device";
    }


    private String detectBrowser(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) return "Unknown Browser";

        String ua = userAgent.toLowerCase();
        if (ua.contains("edg/")) return "Edge";
        if (ua.contains("chrome/")) return ua.contains("mobile") ? "Chrome Mobile" : "Chrome";
        if (ua.contains("safari/") && !ua.contains("chrome")) return "Safari";
        if (ua.contains("firefox/")) return "Firefox";
        return "Unknown Browser";
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void printLog(JwtToken jwtToken, DeviceInfo deviceInfo) {
        log.info("Device Info: {}", deviceInfo.toString());
        log.info("Device ID: {}", deviceInfo.getDeviceId());
        log.info("Device Type: {}", deviceInfo.getDeviceType());
        log.info("Device Name: {}", deviceInfo.getDeviceName());
        log.info("Device IP Address: {}", deviceInfo.getIpAddress());
        log.info("Device User-Agent: {}", deviceInfo.getUserAgent());
        log.info("JWT Token: {}", jwtToken);
    }
}
