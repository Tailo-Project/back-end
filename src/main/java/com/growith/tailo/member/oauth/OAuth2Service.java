package com.growith.tailo.member.oauth;



import com.growith.tailo.member.dto.response.KakaoUserInfo;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final RestTemplate restTemplate;


    public String validateIdToken(String idToken) throws Exception {
        JWT jwt = JWTParser.parse(idToken);

        // 예시로 Google API에서 정보를 가져와 검증
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        String response = restTemplate.getForObject(url, String.class);

        // email을 response에서 추출
        String email = extractEmailFromResponse(response);

        return email;
    }

    private String extractEmailFromResponse(String response) {
        // JSON 파싱 후 email 반환
        return "user@example.com"; // 예시로 반환
    }



    public KakaoUserInfo getKakaoUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    KakaoUserInfo.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("카카오 로그인 에러" , e);
            throw new RuntimeException("카카오 로그인 실패", e);
        }
    }
}
