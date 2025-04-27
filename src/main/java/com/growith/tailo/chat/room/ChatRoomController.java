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

    @Operation(summary = "채팅방 생성", description = "로그인 멤버와 요청 멤버의 accountID 로 채팅방 조회 후 생성")
    @GetMapping
    public ApiResponse<ChatRoom> create(@AuthenticationPrincipal Member member, @RequestParam("accountId") String accountId ){
        ChatRoom chatRoom = chatRoomService.getChatRoom(member,accountId);
        return ApiResponses.success(chatRoom);
    }

}
