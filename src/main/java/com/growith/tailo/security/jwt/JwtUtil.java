package com.growith.tailo.security.jwt;

import com.growith.tailo.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@Component
public class JwtUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Value("${spring.jwt.secret}")
    private String secret;
    @Value("${spring.jwt.issuer}")
    private String issuer;
    @Value("${spring.jwt.accessTokenExpirationTime}")
    private long accessTokenExpirationTime;
    @Value("${spring.jwt.refreshTokenExpirationTime}")
    private long refreshTokenExpirationTime;


    // 시크릿 키를 바이트 배열로 변환하여 Key 객체 반환

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    // 토큰에서 사용자 이름(subject) 추출

    public String getUsernameFromToken(String token) {
        log.debug("토큰에서 사용자 이름 추출 시도");
        return getClaimFromToken(token, Claims::getSubject);
    }

    //토큰에서 만료 일자 추출
    public Date getExpirationDateFromToken(String token) {
        log.debug("토큰에서 만료 날짜 추출 시도");
        return getClaimFromToken(token, Claims::getExpiration);
    }


    // 클레임에서 필요한 값만 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //JWT 토큰에서 모든 Claims 추출
    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException {
        try {
            log.debug("토큰에서 모든 Claims 파싱 시도");
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT 파싱 오류 발생", e);
            throw new RuntimeException("잘못된 JWT 토큰입니다.");
        }
    }


    //토큰 만료 여부 확인
    public Boolean isTokenExpired(String token) {
        boolean expired = getExpirationDateFromToken(token).before(new Date());
        log.debug("토큰 만료 여부: {}", expired);
        return expired;
    }


    // Access Token 생성

    public String generateAccessToken(Member member) {
        log.info("Access Token 생성 시작 for 사용자: {}", member.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("accountId", member.getUsername());
        claims.put("role", member.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));
        return doGenerateAccessToken(claims, member.getUsername());
    }


    // 실제 Access Token 생성 로직
    private String doGenerateAccessToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        log.info("Access Token 생성 완료: {}", token);
        return token;
    }


    // 토큰 검증 (username 일치 & 만료되지 않음)
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUsernameFromToken(token);
        boolean valid = userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
        log.info("토큰 유효성 검사 결과: {}", valid);
        return valid;
    }


    // Refresh Token 생성
    public String generateRefreshToken(UserDetails userDetails) {
        log.info("Refresh Token 생성 시작 for 사용자: {}", userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("accountId", userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));
        return doGenerateRefreshToken(claims, userDetails.getUsername());
    }


    // 실제 Refresh Token 생성 로직
    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        log.info("Refresh Token 생성 완료: {}", token);
        return token;
    }


    // Refresh 토큰의 유효성 검사 (만료 여부만 확인)

    public Boolean validateRefreshToken(String token) {
        boolean valid = !isTokenExpired(token);
        log.debug("Refresh 토큰 유효성 검사 결과: {}", valid);
        return valid;
    }
}
