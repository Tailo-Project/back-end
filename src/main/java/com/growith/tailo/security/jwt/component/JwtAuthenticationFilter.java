package com.growith.tailo.security.jwt.component;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.security.jwt.entity.RefreshToken;
import com.growith.tailo.security.jwt.repository.RefreshTokenRepository;
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
import java.util.Optional;

// 클라이언트 요청마다 실행되는 필터 JWT로 사용자 인증 체크
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String accessToken = null;
        String memberAccountId = null;
        String tokenPrefix = "Bearer ";

        if (header != null && header.startsWith(tokenPrefix)) {
            accessToken = header.substring(tokenPrefix.length());

            try {
                // 액세스 토큰에서 사용자 ID 추출
                memberAccountId = jwtUtil.getUsernameFromToken(accessToken);
                log.info("정상 토큰, 사용자 추출: {}", memberAccountId);

            } catch (ExpiredJwtException e) {
                log.warn("액세스 토큰 만료: {}", e.getMessage());
                memberAccountId = e.getClaims().getSubject();

                Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByAccountId(memberAccountId);

                if (optionalRefreshToken.isPresent() &&
                        jwtUtil.validateRefreshToken(optionalRefreshToken.get().getToken())) {

                    log.info("리프레시 토큰 유효, 새 액세스 토큰 발급 진행");

                    UserDetails userDetails = userDetailService.loadUserByUsername(memberAccountId);
                    String newAccessToken = jwtUtil.generateAccessToken((Member) userDetails);

                    // 클라이언트에 새 토큰 전달
                    response.setHeader("Authorization", tokenPrefix + newAccessToken);
                    log.info("새로운 AccessToken 발급 완료: {}", newAccessToken);

                    //  인증 객체 SecurityContext 에 등록
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    log.error("리프레시 토큰이 존재하지 않거나 유효하지 않음");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("로그인이 만료되었습니다. 다시 로그인 해주세요.");
                    response.getWriter().flush();
                    return;
                }

            } catch (Exception e) {
                log.error("액세스 토큰 검증 오류", e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("토큰 검증 중 오류가 발생했습니다.");
                response.getWriter().flush();
                return;
            }
        } else {
            log.debug("Authorization 헤더 없음 또는 잘못된 형식");
        }

        // 인증 정보가 없다면 인증 객체 설정 (최초 인증 요청일 경우)
        if (memberAccountId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(memberAccountId);

            if (jwtUtil.validateToken(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("토큰은 존재하나 유효성 검증 실패");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("인증 정보가 유효하지 않습니다.");
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}