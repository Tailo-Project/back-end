package com.growith.tailo.member.controller;

import com.growith.tailo.member.dto.request.SignUpRequest;
import com.growith.tailo.member.dto.request.SocialLoginRequest;
import com.growith.tailo.member.dto.response.LoginResponse;
import com.growith.tailo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth/sign-in")
    public ResponseEntity<LoginResponse> socialLogin(@RequestBody SocialLoginRequest request) throws Exception {
       LoginResponse loginResponse =  memberService.socialLoginService(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }
//    @PostMapping("/auth-login")
//    public ResponseEntity<?> loginWithGoogle(@RequestBody String accessToken) {
//        // 구글 API 엔드포인트
//        String googleApiUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
//
//        // 구글 사용자 정보 요청
//        ResponseEntity<String> response = restTemplate.exchange(
//                googleApiUrl,
//                HttpMethod.GET,
//                null,
//                String.class
//        );
//
//        // 사용자 정보 받아오기
//        String userInfo = response.getBody();
//
//        // 사용자 정보를 기반으로 추가 로직 (예: JWT 발급 등)
//        // 예시로 userInfo를 그냥 반환
//        return ResponseEntity.ok(userInfo);
//    }
    @PostMapping("/auth/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request){
        String message = memberService.signUpService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
