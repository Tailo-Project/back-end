package com.growith.tailo.chat.room.repository;

import com.growith.tailo.chat.member.entity.QChatMember;
import com.growith.tailo.chat.room.dto.response.ChatRoomResponse;
import com.growith.tailo.chat.room.dto.response.ChatRoomSimpleResponse;
import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.chat.room.entity.QChatRoom;
import com.growith.tailo.member.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomDSLRepository{
    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<ChatRoom> findDirectChatRoom(Member member, Member selectMember) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QChatMember chatMember = QChatMember.chatMember;

        // 두 멤버가 각각 참여한 채팅방을 찾는 쿼리
        ChatRoom room = queryFactory
                .select(chatRoom)
                .from(chatRoom)
                .join(chatRoom.chatMembers, chatMember)
                .where(
                        chatMember.member.eq(member)
                                .and(chatRoom.chatMembers.any().member.eq(selectMember))) // 두 멤버가 모두 참여한
                .groupBy(chatRoom.id) // roomId별로 그룹화
                .having(chatRoom.chatMembers.size().eq(2)) // 멤버 수가 정확히 2인 채팅방만 찾기
                .fetchOne(); // 결과 하나만 가져옴

        return Optional.ofNullable(room);
    }

    @Override
    public Page<ChatRoomSimpleResponse> findAllChatRoom(Member member, Pageable pageable) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QChatMember chatMember = QChatMember.chatMember;

        List<ChatRoom> rooms = queryFactory
                .select(chatRoom)
                .from(chatRoom)
                .join(chatRoom.chatMembers, chatMember)
                .where(chatMember.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ChatRoomSimpleResponse> content = rooms.stream()
                .map(ChatRoomSimpleResponse::fromEntity)
                .toList();

        // 전체 개수 쿼리
        Long totalCount = queryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .join(chatRoom.chatMembers, chatMember)
                .where(chatMember.member.eq(member))
                .fetchOne();
        long total = totalCount != null ? totalCount : 0L;
        return new PageImpl<>(content, pageable, total);
    }
}
