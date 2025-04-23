package com.growith.tailo.security.jwt.service;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import com.growith.tailo.security.jwt.component.JwtUtil;
import com.growith.tailo.security.jwt.entity.RefreshToken;
import com.growith.tailo.security.jwt.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public String createNewAccess(String accountId, HttpServletRequest request){
        log.error("Request IP: {}", request.getRemoteAddr());
        log.error("Request URI: {}", request.getRequestURI());
        Member member = memberRepository.findByAccountId(accountId).orElseThrow(
                ()-> new ResourceNotFoundException("현재 로그인한 유저 정보가 없습니다.")
        );
        RefreshToken refreshToken = refreshTokenRepository.findByAccountId(member.getAccountId()).orElseThrow(
                ()-> new ResourceNotFoundException("로그인이 필요합니다.") // 주요정보 노출 방지 ( 리프레시 토큰 없음 )
        );
        log.error("리프레시 토큰 없음 로그인 필요");
        if (!jwtUtil.validateRefreshToken(refreshToken.getToken())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"로그인이 필요합니다."); //( 토큰 만료 )
        }
        log.error("리프레시 만료 없음 로그인 필요");


        return jwtUtil.generateAccessToken(member);
    }
}