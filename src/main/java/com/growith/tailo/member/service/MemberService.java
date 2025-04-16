package com.growith.tailo.member.service;

import com.growith.tailo.common.exception.ResourceAlreadyExistException;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.dto.request.SignUpRequest;
import com.growith.tailo.member.dto.request.SocialLoginRequest;
import com.growith.tailo.member.dto.request.UpdateRequest;
import com.growith.tailo.member.dto.response.KakaoUserInfo;

import com.growith.tailo.member.dto.response.LoginResponse;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.mapper.to.MemberMapper;
import com.growith.tailo.member.oauth.OAuth2Service;
import com.growith.tailo.member.repository.MemberRepository;
import com.growith.tailo.security.jwt.JwtUtil;
import com.growith.tailo.security.jwt.entity.RefreshToken;
import com.growith.tailo.security.jwt.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2Service oAuth2Service;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse socialLoginService(SocialLoginRequest request) throws Exception {
        String email;

        // 소셜 정보 추출
        if ("google".equals(request.provider())) {

            email = oAuth2Service.validateIdToken(request.accessToken());
        } else if ("kakao".equals(request.provider())) {
            KakaoUserInfo userInfo = oAuth2Service.getKakaoUserInfo(request.accessToken());
            email = userInfo.id(); // ← KakaoUserInfo 클래스에 getEmail()이 있어야 함
        } else {
            throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다.");
        }

        // email 기준으로 기존 회원 여부 확인
        Member member = memberRepository.findByEmail(email).orElse(null);

        if (member == null) {
            // 회원 정보가 없으면 token 에 null 값 반환
            return new LoginResponse(email, null);
        }

        // 기존 회원 → 로그인 처리
        String accessToken = jwtUtil.generateAccessToken(member);
        String refreshToken = jwtUtil.generateRefreshToken(member);

        // 기존 회원 로그인 시 DB 리프레시 토큰 갱신
        // 리프레시 토큰을 사용자 어카운트 아이디와 같이 디비에 저장
        refreshTokenRepository.findByAccountId(member.getAccountId())
                .ifPresent(refreshTokenRepository::delete);
        refreshTokenRepository.save(RefreshToken.builder()
                .accountId(member.getAccountId())
                .token(refreshToken)
                .build());

        return new LoginResponse(email, accessToken);
    }
    @Transactional
    public String signUpService(SignUpRequest signUpRequest) {

        Member signUpMember = MemberMapper.signUpToEntity(signUpRequest);
        memberRepository.save(signUpMember);
        return "회원 가입 성공";
    }
    // accountId 중복 확인 로직
    public void validateAccountId(String accountId) {
        if (memberRepository.existsByAccountId(accountId)) {
            throw new ResourceAlreadyExistException("중복된 아이디입니다.");
        }
    }
    // 프로필 변경 로직
    @Transactional
    public String updateProfile(Member member, UpdateRequest updateRequest){
        try {
            //JPA 영속성 컨텍스트에 등록
            Member updateMember = memberRepository.findById(member.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("해당 회원이 존재하지 않습니다."));

            updateMember.updateProfile(updateRequest);  // Dirty Checking OK (영속 객체)
            return "업데이트 성공";

        } catch (Exception e) {
            log.error("업데이트 실패", e);
            throw new RuntimeException("업데이트 실패");
        }
    }
}

