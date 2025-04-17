package com.growith.tailo.member.controller;

import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.member.dto.request.SignUpRequest;
import com.growith.tailo.member.dto.request.SocialLoginRequest;
import com.growith.tailo.member.dto.request.UpdateRequest;
import com.growith.tailo.member.dto.response.LoginResponse;
import com.growith.tailo.member.dto.response.MemberDetailResponse;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth/sign-in")
    public ResponseEntity<LoginResponse> socialLogin(@RequestBody SocialLoginRequest request) throws Exception {
        LoginResponse loginResponse = memberService.socialLoginService(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<ApiResponse<String>> signUp(@RequestBody SignUpRequest request) {
        String message = memberService.signUpService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponses.created(message));
    }

    @GetMapping("/member/duplicate/{accountId}")
    public ResponseEntity<ApiResponse<String>> duplicateAccount(@PathVariable String accountId) {
        memberService.validateAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success("사용 가능한 아이디입니다."));
    }

    @PatchMapping("/member")
    public ResponseEntity<MemberDetailResponse> update(@AuthenticationPrincipal Member member, @RequestBody UpdateRequest updateRequest) {
        MemberDetailResponse response = memberService.updateProfile(member, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
