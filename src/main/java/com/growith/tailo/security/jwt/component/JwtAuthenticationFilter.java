package com.growith.tailo.security.jwt.component;

import com.growith.tailo.security.jwt.service.CustomUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 클라이언트 요청마다 실행되는 필터 JWT로 사용자 인증 체크
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if(request.getRequestURI().startsWith("/api/auth")){
            filterChain.doFilter(request,response);
            return;
        }

        String header = request.getHeader("Authorization");
        String accessToken = null;
        String memberAccountId = null;
        String tokenPrefix = "Bearer ";
        // 리퀘스트 헤더에서 토큰만 파싱
        if (header != null && header.startsWith(tokenPrefix)) {
            accessToken = header.substring(tokenPrefix.length());

            try {
                // 액세스 토큰에서 사용자 ID 추출
                memberAccountId = jwtUtil.getUsernameFromToken(accessToken);
                log.info("정상 토큰, 사용자 추출: {}", memberAccountId);

            } catch (ExpiredJwtException e) {
                log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write("{\"message\": \"재 요청이 필요합니다.\"}");
                response.getWriter().flush();
                return;
            }
        }
        // 인증 정보가 없다면 인증 객체 설정 (최초 인증 요청일 경우)
        if (memberAccountId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(memberAccountId);

            if (jwtUtil.validateAccessToken(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("토큰은 존재하나 유효성 검증 실패");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write("{\"message\": \"회원 정보가 올바르지 않습니다.\"}");
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
