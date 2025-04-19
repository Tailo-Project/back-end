package com.growith.tailo.member.service;

import com.growith.tailo.common.exception.ResourceAlreadyExistException;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.common.handler.ImageUploadHandler;
import com.growith.tailo.member.dto.request.SignUpRequest;
import com.growith.tailo.member.dto.request.SocialLoginRequest;
import com.growith.tailo.member.dto.request.UpdateRequest;
import com.growith.tailo.member.dto.response.KakaoUserInfo;
import com.growith.tailo.member.dto.response.LoginResponse;
import com.growith.tailo.member.dto.response.MemberDetailResponse;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.mapper.from.FromMemberMapper;
import com.growith.tailo.member.mapper.to.ToMemberMapper;
import com.growith.tailo.member.oauth.OAuth2Service;
import com.growith.tailo.member.repository.MemberRepository;
import com.growith.tailo.security.jwt.JwtUtil;
import com.growith.tailo.security.jwt.entity.RefreshToken;
import com.growith.tailo.security.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2Service oAuth2Service;
    private final JwtUtil jwtUtil;
    private final ImageUploadHandler imageUploadHandler;

    @Transactional
    public LoginResponse socialLoginService(SocialLoginRequest request) {
        String email;

        if ("google".equals(request.provider())) {
            email = oAuth2Service.validateIdToken(request.accessToken());
        } else if ("kakao".equals(request.provider())) {
            KakaoUserInfo userInfo = oAuth2Service.getKakaoUserInfo(request.accessToken());
            log.info("userInfo: {}", userInfo);
            email = userInfo.id();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 로그인 방식");
        }

        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            return FromMemberMapper.fromMemberLogin(email, null);
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
    public String signUpService(SignUpRequest signUpRequest, MultipartFile profileImage) {
        validateAccountId(signUpRequest.accountId());
        String imageUrl = null;

        // 이미지 저장
        if (profileImage != null && !profileImage.isEmpty()) {
            imageUrl = saveImage(profileImage); // 이미지를 저장하고 URL을 할당
        }

        Member signUpMember = ToMemberMapper.signUpToEntity(signUpRequest, imageUrl);
        memberRepository.save(signUpMember);

        return "회원 가입 성공";
    }

    public void validateAccountId(String accountId) {
        if (memberRepository.existsByAccountId(accountId)) {
            throw new ResourceAlreadyExistException("중복된 아이디입니다.");
        }
    }

    @Transactional
    public MemberDetailResponse updateProfile(Member member, UpdateRequest updateRequest, MultipartFile profileImage) {
        if (member == null || !memberRepository.existsById(member.getId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }
        validateAccountId(updateRequest.accountId());
        // 기존 이미지 삭제 처리
        if (profileImage != null && !profileImage.isEmpty()) {
            String oldImageUrl = member.getProfileImageUrl();
            if (oldImageUrl != null) {
                imageUploadHandler.deleteImage(oldImageUrl); // 기존 이미지 삭제
            }

            // 새 이미지 저장
            String imageUrl = saveImage(profileImage);
            member.setProfileImageUrl(imageUrl); // 새로운 이미지 URL 할당
        }

        // 프로필 업데이트
        member.updateProfile(updateRequest);

        // DB에 저장
        memberRepository.save(member);

        return FromMemberMapper.fromMemberDetail(member);
    }

    // 이미지 저장
    private String saveImage(MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty()) {
            return null;
        }
        return imageUploadHandler.uploadSingleImages(profileImage);
    }

}

