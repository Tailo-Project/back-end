package com.growith.tailo.member.oauth;



import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.dto.response.KakaoUserInfo;
import org.apache.coyote.BadRequestException;
import org.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final RestTemplate restTemplate;


    public String validateIdToken(String idToken) throws Exception {
        // 예시로 Google API에서 정보를 가져와 검증
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        String response = restTemplate.getForObject(url, String.class);

        // email을 response에서 추출
        String email = extractEmailFromResponse(response);

        return email;
    }

    private String extractEmailFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("email");
        } catch (Exception e) {
            log.error("이메일 추출 실패: ", e);
            throw new RuntimeException("응답에서 이메일을 추출할 수 없습니다.");
        }
    }


    public KakaoUserInfo getKakaoUserInfo(String accessToken) throws BadRequestException {
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
            log.error("소셜 로그인 타임아웃 또는 연결 실패", e);
            throw new BadRequestException("소셜 로그인 실패: 타임아웃 또는 연결 문제");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // 4xx 또는 5xx 에러
            log.error("소셜 로그인 요청 실패", e);
            throw new RuntimeException("소셜 로그인 요청 실패: " + e.getStatusCode());
        }
    }

}
