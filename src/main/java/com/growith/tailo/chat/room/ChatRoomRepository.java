package com.growith.tailo.chat.room;

import com.growith.tailo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByChatMember1AndChatMember2OrChatMember1AndChatMember2(Member chatMember1, Member chatMember2, Member chatMember3, Member chatMember4);

}
