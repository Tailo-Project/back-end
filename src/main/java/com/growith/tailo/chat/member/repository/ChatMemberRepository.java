package com.growith.tailo.chat.member.repository;

import com.growith.tailo.chat.member.entity.ChatMember;
import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    Optional<ChatMember> findByMember(Member member);

    Optional<ChatMember> findByMemberAndChatRoom(Member member, ChatRoom chatRoom);
}
