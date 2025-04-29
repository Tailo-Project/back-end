package com.growith.tailo.chat.message.repository;

import com.growith.tailo.chat.message.entity.ChatMessage;
import com.growith.tailo.chat.room.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findAllByChatRoom(ChatRoom chatRoom, Pageable pageable);
}
