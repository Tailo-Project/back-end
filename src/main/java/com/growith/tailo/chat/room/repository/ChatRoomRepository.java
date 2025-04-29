package com.growith.tailo.chat.room.repository;

import com.growith.tailo.chat.member.entity.ChatMember;
import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,ChatRoomDSLRepository {
    Optional<ChatRoom> findDirectChatRoom(Member member, Member selectMember);
}
