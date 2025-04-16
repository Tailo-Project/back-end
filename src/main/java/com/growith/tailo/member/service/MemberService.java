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

        if ("google".equals(request.provider())) {
            email = oAuth2Service.validateIdToken(request.accessToken());
        } else if ("kakao".equals(request.provider())) {
            KakaoUserInfo userInfo = oAuth2Service.getKakaoUserInfo(request.accessToken());
            email = userInfo.id();
        } else {
            throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다.");
        }

        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            return new LoginResponse(email, null);
        }

        String accessToken = jwtUtil.generateAccessToken(member);
        String refreshToken = jwtUtil.generateRefreshToken(member);

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
        if (memberRepository.existsByAccountId(signUpRequest.accountId())) {
            throw new ResourceAlreadyExistException("이미 존재하는 아이디입니다.");
        }
        String fileName = signUpRequest.file().getOriginalFilename();
        Member signUpMember = MemberMapper.signUpToEntity(signUpRequest);
        signUpMember.setProfileImageUrl(fileName);
        memberRepository.save(signUpMember);
        return "회원 가입 성공";
    }

    public void validateAccountId(String accountId) {
        if (memberRepository.existsByAccountId(accountId)) {
            throw new ResourceAlreadyExistException("중복된 아이디입니다.");
        }
    }

    @Transactional
    public String updateProfile(Member member, UpdateRequest updateRequest){
        Member updateMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 회원이 존재하지 않습니다."));

        updateMember.updateProfile(updateRequest);
        return "업데이트 성공";
    }
}


