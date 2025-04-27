package com.growith.tailo.chat.room;

import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 조회", description = "로그인 멤버와 요청 멤버와 채팅방 ID 로 채팅방 조회")
    @GetMapping("/{roomId}")
    public ApiResponse<ChatRoomResponse> getRoom(@AuthenticationPrincipal Member member, @PathVariable("roomId")Long roomId ){
        ChatRoomResponse chatRoom = chatRoomService.getChatRoom(member,roomId);
        return ApiResponses.success(chatRoom);
    }

    @Operation(summary = "채팅방 생성", description = "로그인 멤버와 대상 멤버의 accountId 로 채팅방 생성")
    @PostMapping
    public ApiResponse<ChatRoomResponse> create(@AuthenticationPrincipal Member member, @RequestParam("accountId")String accountId){
        ChatRoomResponse chatRoom = chatRoomService.createRoom(member, accountId);
        return ApiResponses.success(chatRoom);
    }

}
