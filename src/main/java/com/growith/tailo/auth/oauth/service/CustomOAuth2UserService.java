package com.growith.tailo.auth.oauth.service;

import com.growith.tailo.auth.jwt.service.CustomUserDetails;
import com.growith.tailo.auth.oauth.dto.KakaoOAuth2UserInfo;
import com.growith.tailo.auth.oauth.dto.OAuth2UserInfo;
import com.growith.tailo.auth.oauth.entity.OAuthProvider;
import com.growith.tailo.user.entity.User;
import com.growith.tailo.user.enums.Role;
import com.growith.tailo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase());
        return processOAuth2User(provider, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuthProvider provider, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        log.info("OAUTH2USERINFO ABSTRACT CLASS FROM CUSTOM OAUTH2 USER SERVICE: {}", oAuth2User);

        // 사용자 처리 로직
        // TODO: 추후 map() UserUpdateRequest로 변경 필요, SuccessHandler 참고
        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .map(u -> u.update(oAuth2UserInfo))
                .orElseGet(() -> User.builder()
                        .email(oAuth2UserInfo.getEmail())
                        .nickName(oAuth2UserInfo.getName())
                        .profileImageUrl(oAuth2UserInfo.getImageUrl())
                        .provider(provider)
                        .providerId(oAuth2UserInfo.getId())
                        .role(Role.USER)
                        .emailVerified(true)
                        .build());
        userRepository.save(user);
        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

    private OAuth2UserInfo getOAuth2UserInfo(OAuthProvider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + provider.name());
        };
    }

}
