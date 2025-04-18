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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member", description = "회원 관리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 통해 토큰 발급")
    @PostMapping("/auth/sign-in")
    public ResponseEntity<ApiResponse<LoginResponse>> socialLogin(@RequestBody SocialLoginRequest request) {
        LoginResponse loginResponse = memberService.socialLoginService(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(loginResponse));
    }

    @PostMapping(value = "/auth/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> signUp(
            @RequestPart("request") @Valid SignUpRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    )  {
        String message = memberService.signUpService(request,profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponses.created(message));
    }

    @Operation(summary = "아이디 중복 확인", description = "사용할 수 있는 아이디인지 확인")
    @GetMapping("/member/duplicate/{accountId}")
    public ResponseEntity<ApiResponse<String>> duplicateAccount(@PathVariable String accountId) {
        memberService.validateAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success("사용 가능한 아이디입니다."));
    }

    @Operation(summary = "프로필 수정", description = "회원의 프로필을 수정")
    @PatchMapping("/member")
    public ResponseEntity<ApiResponse<MemberDetailResponse>> update(@AuthenticationPrincipal Member member, @RequestBody UpdateRequest updateRequest) {
        MemberDetailResponse response = memberService.updateProfile(member, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(response));
    }
}