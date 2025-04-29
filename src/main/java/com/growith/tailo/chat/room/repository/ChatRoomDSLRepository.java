package com.growith.tailo.chat.room.repository;

import com.growith.tailo.chat.room.dto.response.ChatRoomListResponse;
import com.growith.tailo.chat.room.dto.response.ChatRoomResponse;
import com.growith.tailo.chat.room.dto.response.ChatRoomSimpleResponse;
import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChatRoomDSLRepository {
    Optional<ChatRoom> findDirectChatRoom(Member member, Member selectMember);
    Page<ChatRoomSimpleResponse> findAllChatRoom(Member member, Pageable pageable);
}
