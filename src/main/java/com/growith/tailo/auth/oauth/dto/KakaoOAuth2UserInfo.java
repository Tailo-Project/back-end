package com.growith.tailo.auth.oauth.dto;

import java.util.Map;

@SuppressWarnings("unchecked")
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private final Map<String, Object> account;
    private final Map<String, Object> profile;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.account = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) account.get("profile");
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        return String.valueOf(profile.get("nickname"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(account.get("email"));
    }

    @Override
    public String getImageUrl() {
        return String.valueOf(profile.get("profile_image_url"));
    }

}