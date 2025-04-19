package com.growith.tailo.block.controller;

import com.cloudinary.Api;
import com.growith.tailo.block.dto.response.BlockListResponse;
import com.growith.tailo.block.service.BlockService;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/block")
@Tag(name = "BlockMember", description = "차단 회원 관리 API")
public class BlockMemberController {
    private final BlockService blockService;

    @Operation(summary = "차단한 사람 조회", description = "내가 차단한 사람 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BlockListResponse>>> getListBlocked(@AuthenticationPrincipal Member member){
        List<BlockListResponse> list = blockService.getListService(member);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success("차단 목록 조회",list));
    }

    @Operation(summary = "차단하기", description = "사용자 조회 후 차단등록")
    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> blockingMember(@AuthenticationPrincipal Member member, @PathVariable String accountId){
        String message = blockService.blockingMemberService(member,accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(message));
    }

    @Operation(summary = "차단 해제", description = "차단한 사용자 조회 후 삭제")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> deleteBlocked(@AuthenticationPrincipal Member member, @PathVariable String accountId){
        String message = blockService.deleteBlockedService(member, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(message));
    }
}
