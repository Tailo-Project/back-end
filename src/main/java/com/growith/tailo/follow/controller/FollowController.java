package com.growith.tailo.follow.controller;

import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.follow.dto.response.FollowListResponse;
import com.growith.tailo.follow.dto.response.FollowMeResponse;
import com.growith.tailo.follow.dto.response.MyFollowResponse;
import com.growith.tailo.follow.service.FollowService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/follow")
@Tag(name = "Follow", description = "팔로우 관리 API")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우 요청", description = "상대방을 팔로우 하는 요청입니다.")
    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> memberFollow(@AuthenticationPrincipal Member member,@PathVariable("accountId") String accountId){
        String message = followService.follow(member,accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(message));
    }

    @Operation(summary = "팔로우 취소", description = "팔로우를 취소(삭제)하는 요청")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> followCancel(@AuthenticationPrincipal Member member, @PathVariable("accountId")String accountId){
        String message = followService.followCancel(member,accountId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(message));
    }

    @Operation(summary = "팔로우 조회", description = "멤버가 팔로우한 리스트 조회")
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<FollowListResponse>> followList(@PathVariable("accountId")String accountId,
                                                                            Pageable pageable){
        Page<MyFollowResponse> list = followService.getFollowList(accountId,pageable);
        FollowListResponse response = new FollowListResponse(list.getContent(),new Pagination(
                list.getNumber(),
                list.getSize(),
                list.getTotalPages(),
                list.getTotalElements()));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success( accountId+" 팔로잉 리스트",response));
    }
    @Operation(summary = "팔로잉 조회", description = "멤버를 팔로잉한 리스트 조회")
    @GetMapping("/{accountId}/following")
    public ResponseEntity<ApiResponse<FollowListResponse>> targetList(@PathVariable("accountId")String accountId,
                                                                      Pageable pageable){
        Page<FollowMeResponse> list = followService.getTargetList(accountId,pageable);
        FollowListResponse response = new FollowListResponse(list.getContent(),new Pagination(
                list.getNumber(),
                list.getSize(),
                list.getTotalPages(),
                list.getTotalElements()));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponses.success(accountId+"팔로워 리스트",response));
    }
}

