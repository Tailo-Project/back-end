package com.growith.tailo.chat.message.controller;

import com.growith.tailo.chat.message.dto.ChatMessageListResponse;
import com.growith.tailo.chat.message.dto.ChatMessageResponse;
import com.growith.tailo.chat.message.service.ChatMessageService;
import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.common.dto.response.ApiResponse;
import com.growith.tailo.common.util.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/message")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/{roomId}")
    public ApiResponse<ChatMessageListResponse> getList(@PathVariable("roomId") Long roomId, Pageable pageable){
        Page<ChatMessageResponse> chatList = chatMessageService.getChatList(roomId, pageable);

        ChatMessageListResponse response = new ChatMessageListResponse(chatList.getContent(),new Pagination(
                chatList.getNumber(),
                chatList.getSize(),
                chatList.getTotalPages(),
                chatList.getTotalElements())
        );
        return ApiResponses.success(response);
    }
}
