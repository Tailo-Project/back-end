package com.growith.tailo.member.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growith.tailo.member.dto.response.KakaoUserInfo;
import org.apache.coyote.BadRequestException;
import org.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.*;


import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String validateIdToken(String idToken) {
        // 예시로 Google API에서 정보를 가져와 검증
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

        try {
            String response = restTemplate.getForObject(url, String.class);
            return extractEmailFromResponse(response);
        } catch (Exception e) {
            log.error("Google ID 토큰 검증 실패", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ID 토큰 검증 실패");
        }
    }

    private String extractEmailFromResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("email").asText();
        } catch (Exception e) {
            log.error("이메일 추출 실패", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "응답에서 이메일을 추출할 수 없습니다.");
        }
    }


    public KakaoUserInfo getKakaoUserInfo(String accessToken)  {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserInfo> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, KakaoUserInfo.class);

            return response.getBody();

        } catch (ResourceAccessException e) {
            // 타임아웃 또는 연결 문제
            log.error("카카오 로그인 타임아웃 또는 연결 실패", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카카오 로그인 실패: 타임아웃 또는 연결 문제");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // 4xx 또는 5xx 에러
            log.error("카카오 로그인 요청 실패", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카카오 사용자 정보 추출 실패");
        }
    }

}
