package com.growith.tailo.chat.room.conrtoller;

import com.growith.tailo.chat.room.dto.response.ChatRoomListResponse;
import com.growith.tailo.chat.room.dto.response.ChatRoomResponse;
import com.growith.tailo.chat.room.dto.response.ChatRoomSimpleResponse;
import com.growith.tailo.chat.room.service.ChatRoomService;
import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import com.growith.tailo.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 조회", description = "로그인 멤버와 요청 멤버와 채팅방 ID 로 채팅방 조회")
    @GetMapping("/{roomId}")
    public ApiResponse<ChatRoomResponse> getRoom(@AuthenticationPrincipal Member member, @PathVariable("roomId")Long roomId ){
        ChatRoomResponse chatRoom = chatRoomService.getRoom(member,roomId);
        return ApiResponses.success(chatRoom);
    }

    @Operation(summary = "채팅방 전체 조회", description = "멤버가 참여한 채팅방 전체 조회")
    @GetMapping
    public ApiResponse<ChatRoomListResponse> getList(@AuthenticationPrincipal Member member, Pageable pageable){
        Page<ChatRoomSimpleResponse> list = chatRoomService.getRoomList(member, pageable);
        ChatRoomListResponse response = new ChatRoomListResponse(
                list.getContent(),
                new Pagination(
                        list.getNumber(),
                        list.getSize(),
                        list.getTotalPages(),
                        list.getTotalElements())
        );
        return ApiResponses.success(response);
    }


    @Operation(summary = "채팅방 생성", description = "로그인 멤버와 추가 멤버의 accountId로 채팅방 생성")
    @PostMapping
    public ApiResponse<ChatRoomResponse> create(@AuthenticationPrincipal Member member, @RequestParam("accountIds") List<String> accountIds){
        ChatRoomResponse chatRoom = chatRoomService.createRoom(member, accountIds);
        return ApiResponses.success(chatRoom);
    }

    @Operation(summary = "채팅방 삭제", description = "로그인 멤버의 채팅방 목록에서 선택 채팅방 삭제")
    @DeleteMapping("/{roomId}")
    public ApiResponse<String> delete(@AuthenticationPrincipal Member member, @PathVariable("roomId")Long roomId){
        String message = chatRoomService.deleteRoom(member,roomId);
        return ApiResponses.success(message);
    }

}
