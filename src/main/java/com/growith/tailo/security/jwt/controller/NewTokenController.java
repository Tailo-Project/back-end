package com.growith.tailo.security.jwt.controller;

import com.growith.tailo.security.jwt.service.NewTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Token", description = "토큰 요청 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NewTokenController {
    private final NewTokenService newTokenService;

    @Operation(summary = "액세스 토큰 재발급", description = "accountId 를 통한 액세스토큰 재발급")
    @PostMapping("/auth/token")
    public ResponseEntity<?> create(
            @RequestParam("accountId") String accountId,
            HttpServletRequest request
    ) {
        String newAccessToken = newTokenService.createNewAccess(accountId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("accessToken", newAccessToken));
    }
}