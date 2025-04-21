package com.growith.tailo.block.controller;

import com.growith.tailo.block.dto.response.BlockListResponse;
import com.growith.tailo.block.dto.response.BlockResponse;
import com.growith.tailo.block.service.BlockService;
import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ApiResponse<BlockListResponse>> getListBlocked(@AuthenticationPrincipal Member member,
                                                                         Pageable pageable){
        Page<BlockResponse> list = blockService.getListService(member,pageable);
        BlockListResponse response = new BlockListResponse(list.getContent(),new Pagination(
                list.getNumber(),
                list.getSize(),
                list.getTotalPages(),
                list.getTotalElements()
        ));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success("차단 목록 조회",response));
    }

    @Operation(summary = "차단하기", description = "사용자 조회 후 차단등록")
    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> blockingMember(@AuthenticationPrincipal Member member, @PathVariable("accountId") String accountId){
        String message = blockService.blockingMemberService(member,accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(message));
    }

    @Operation(summary = "차단 해제", description = "차단한 사용자 조회 후 삭제")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> deleteBlocked(@AuthenticationPrincipal Member member, @PathVariable("accountId") String accountId){
        String message = blockService.deleteBlockedService(member, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(message));
    }
}
