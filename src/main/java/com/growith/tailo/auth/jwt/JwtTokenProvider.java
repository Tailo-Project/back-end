package com.growith.tailo.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationInMilliseconds;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationInMilliseconds;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    /* 이론 예제 코드, 실무에서 비권장 */
//    /** Access Token 생성 */
//    public String generateAccessToken(Authentication authentication) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        return createToken(
//                customUserDetails.getEmail(),
//                customUserDetails.getRole(),
//                accessTokenExpirationInMilliseconds
//        );
//    }
//
//    /** Refresh Token 생성 */
//    public String generateRefreshToken(Authentication authentication) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        return createToken(
//                customUserDetails.getEmail(),
//                customUserDetails.getRole(),
//                refreshTokenExpirationInMilliseconds
//        );
//    }

    /* Spring Security 공식 문서 및 실무 권장 코드 */
    /** Access Token 생성 */
    public String generateAccessToken(String email, String role) {
        return createToken(email, role, accessTokenExpirationInMilliseconds);
    }

    /** Refresh Token 생성 */
    public String generateRefreshToken(String email, String role) {
        return createToken(email, role, refreshTokenExpirationInMilliseconds);
    }

    /** Token 생성 공통 로직 */
    private String createToken(String subject, String role, Long expiration) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** 토큰에서 Subject 추출 */
    public String getSubjectFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid: {}", e.getMessage());
        }
        return false;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
