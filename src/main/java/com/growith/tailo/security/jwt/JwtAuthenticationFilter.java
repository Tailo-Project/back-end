package com.growith.tailo.security.jwt;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.entity.RefreshToken;
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


    private String tokenPrefix="Bearer ";

    // 매 요청마다 실행 JWT 검사 및 인증을 처리 하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더에서 토큰 추출
        String header = request.getHeader("Authorization");
        String memberAccountId = null;
        String accessToken = null;

        if (header!=null&&header.startsWith(tokenPrefix)){
            // 토큰 접두사 제거
            accessToken = header.substring(tokenPrefix.length());
            try{
                // 토큰에서 사용자 accountId 추출
                memberAccountId = this.jwtUtil.getUsernameFromToken(accessToken);
                log.info("토큰 사용자 추출 : {} ", memberAccountId);
                // 액세스 토큰 만료 시 실행
            }catch (ExpiredJwtException e){
                log.error("액세스 토큰 만료 , 리프레시 토큰 확인");
                memberAccountId = e.getClaims().getSubject();
                log.info("토큰 확인 memberAccountI: {}",memberAccountId);
                // 리프레시 토큰 확인
                Optional<RefreshToken> refreshTokenValid = refreshTokenRepository.findByAccountId(memberAccountId);
                // 리프레시 토큰이 존재하고 리프레시 토큰이 유효한 경우
                if (refreshTokenValid.isPresent()&&this.jwtUtil.validateRefreshToken(refreshTokenValid.get().getToken())){
                    // 새로운 액세스 토큰 생성 로직 진행
                    UserDetails userDetails = this.userDetailService.loadUserByUsername(memberAccountId);
                    String newAccessToken = this.jwtUtil.generateAccessToken((Member) userDetails);
                    // 헤더에 반환
                    response.setHeader("Authorization",tokenPrefix+newAccessToken);
                    log.info("새로운 AccessToken 발급 : {}",newAccessToken);
                }else {
                    // 리프레시 토큰이 유효하지 않을 경우 실행
                    log.error("유효한 리프레시 토큰 없음");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("로그인이 만료되었습니다. 다시 로그인 해주세요");
                    return;
                }
                // 미 처리 오류 발생
            }catch (Exception e){
                log.error("토큰 검증 오류", e );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("토큰 검증 오류");
                return;
            }
        }else {
            log.warn("토큰이 헤더에 포함되어 있지 않거나 잘못된 형식");
        }
        // 사용자 아이디가 존재하고 Security Context 에 인증 정보가 null 일 때
        if (memberAccountId != null && SecurityContextHolder.getContext().getAuthentication()==null){
            // 사용자 정보 로드
            UserDetails userDetails = this.userDetailService.loadUserByUsername(memberAccountId);
            //토큰 유효성 검증
            if(this.jwtUtil.validateToken(accessToken,userDetails)){
                //새로운 인증 토큰 생성
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //인증 세부정보 설정
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Security Context 에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                // 토큰이 유효하지 않을 경우 처리
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("인증 정보가 유효하지 않습니다.");
            }
        }
        // 필터체인 계속 진행
        filterChain.doFilter(request,response);
    }
}
