package com.growith.tailo.member.controller;

import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.member.dto.request.SignUpRequest;
import com.growith.tailo.member.dto.request.SocialLoginRequest;
import com.growith.tailo.member.dto.request.UpdateRequest;
import com.growith.tailo.member.dto.response.LoginResponse;
import com.growith.tailo.member.dto.response.MemberDetailResponse;
import com.growith.tailo.member.dto.response.MemberProfileResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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

    @Operation(summary = "회원가입", description = "JSON과 프로필 이미지를 함께 전송합니다.")
    @PostMapping(value = "/auth/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<LoginResponse>>  signUp(
             @RequestPart("request") SignUpRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        LoginResponse loginResponse = memberService.signUpService(request,profileImage);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(loginResponse));
    }

    @Operation(summary = "아이디 중복 확인", description = "사용할 수 있는 아이디인지 확인")
    @GetMapping("/member/duplicate/{accountId}")
    public ResponseEntity<ApiResponse<String>> duplicateAccount(@PathVariable("accountId") String accountId) {
        memberService.validateAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success("사용 가능한 아이디입니다."));
    }
    @Operation(summary = "회원 프로필", description = "프로필 컴포넌트에 사용 될 api ")
    @GetMapping("/member/profile/{accountId}")
    public ResponseEntity<ApiResponse<MemberProfileResponse>> memberProfile(@AuthenticationPrincipal Member member, @PathVariable("accountId") String accountId){
        MemberProfileResponse memberProfileResponse =memberService.profileService(member,accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success("요청"+member.getAccountId()+": 프로필", memberProfileResponse));
    }

    @Operation(summary = "회원 상세", description = "프로필 수정 페이지 접속 시 표시 될 데이터 용")
    @GetMapping("/member")
    public ResponseEntity<ApiResponse<MemberDetailResponse>> detail(@AuthenticationPrincipal Member member){
        MemberDetailResponse response = memberService.getDetail(member);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(response));
    }

    @Operation(summary = "프로필 수정", description = "회원의 프로필을 수정")
    @PatchMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MemberDetailResponse>> update(@AuthenticationPrincipal Member member,
                                                                    @RequestPart("request") UpdateRequest updateRequest,
                                                                    @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        MemberDetailResponse response = memberService.updateProfile(member, updateRequest, profileImage);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(response));
    }
}