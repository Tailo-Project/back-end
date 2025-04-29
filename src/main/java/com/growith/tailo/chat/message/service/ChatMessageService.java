package com.growith.tailo.chat.message.service;

import com.growith.tailo.chat.message.dto.ChatMessageResponse;
import com.growith.tailo.chat.message.entity.ChatMessage;
import com.growith.tailo.chat.message.repository.ChatMessageRepository;
import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.chat.room.repository.ChatRoomRepository;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Page<ChatMessageResponse> getChatList(Long roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("채팅방이 없습니다."));

        Page<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoom(chatRoom, pageable);

        return chatMessages.map(ChatMessageResponse::fromEntity);
    }

}
