package com.growith.tailo.chat.room.service;

import com.growith.tailo.chat.member.entity.ChatMember;
import com.growith.tailo.chat.member.repository.ChatMemberRepository;
import com.growith.tailo.chat.room.component.QueueManager;
import com.growith.tailo.chat.room.dto.response.ChatRoomResponse;
import com.growith.tailo.chat.room.dto.response.ChatRoomSimpleResponse;
import com.growith.tailo.chat.room.entity.ChatRoom;
import com.growith.tailo.chat.room.repository.ChatRoomRepository;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final QueueManager queueManager;
    private final ChatMemberRepository chatMemberRepository;

    private static final String NOT_FOUND_MEMBER = "해당 멤버를 찾을 수 없습니다.";
    // 해당 채팅방 조회
    public ChatRoomResponse getRoom(Member member, Long roomId) {
        // 채팅방 조회
        log.info("{}번 채팅방 조회", roomId);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("채팅방을 찾을 수 없습니다."));

        // member가 해당 채팅방에 참여한 멤버인지 확인
        boolean isMemberInChatRoom = chatRoom.getChatMembers().stream()
                .anyMatch(chatMember -> chatMember.getMember().getId().equals(member.getId()));

        if (!isMemberInChatRoom) {
            throw new ResourceNotFoundException("해당 채팅방에 참여하지 않은 사용자입니다.");
        }

        queueManager.createChatQueue(roomId);

        return ChatRoomResponse.fromEntity(chatRoom);
    }

    // 전체 채팅방 조회
    public Page<ChatRoomSimpleResponse> getRoomList(Member member, Pageable pageable){
        return chatRoomRepository.findAllChatRoom(member,pageable);
    }
    //채팅방 생성
    public ChatRoomResponse createRoom(Member member, List<String> accountIds) {
        log.info("선택된 accountId: {}", accountIds);
        log.info("accountIds 사이즈: {}", accountIds.size());

        // 한 명만 있을 경우 처리 (이미 채팅방이 존재하는지 확인)
        if (accountIds.size() == 1) {
            String accountId = accountIds.get(0);
            Member selectMember = findMemberByAccountId(accountId);

            // 기존 채팅방이 있으면 반환
            Optional<ChatRoom> existingRoom = chatRoomRepository.findDirectChatRoom(member, selectMember);
            if (existingRoom.isPresent()) {
                return ChatRoomResponse.fromEntity(existingRoom.get());
            }
        }

        // 채팅방 이름 설정
        String roomName = member.getAccountId() + "," + String.join(",", accountIds);

        // 채팅방 객체 생성
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName);  // 생성한 roomName을 설정

        // 채팅방 멤버 추가
        List<ChatMember> chatMembers = addChatMembers(member, accountIds);

        // 채팅방 저장
        chatRoomRepository.save(chatRoom);

        // ChatMember에 채팅방 설정 및 저장
        for (ChatMember chatMember : chatMembers) {
            chatMember.setChatRoom(chatRoom);
        }
        chatMemberRepository.saveAll(chatMembers); // ChatMember 저장

        return ChatRoomResponse.fromEntity(chatRoom);
    }

    // 계정 accountId로 멤버 찾기 (중복 코드 제거)
    private Member findMemberByAccountId(String accountId) {
        return memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MEMBER));
    }

    // 채팅방 멤버 추가
    private List<ChatMember> addChatMembers(Member member, List<String> accountIds) {
        List<ChatMember> chatMembers = new ArrayList<>();
        // 로그인한 멤버 추가
        ChatMember loginMember = new ChatMember();
        loginMember.setMember(member);
        chatMembers.add(loginMember);

        // 선택된 멤버들 추가
        for (String accountId : accountIds) {
            Member selectMember = findMemberByAccountId(accountId);
            ChatMember groupChatMember = new ChatMember();
            groupChatMember.setMember(selectMember);
            chatMembers.add(groupChatMember);
        }

        return chatMembers;
    }

    // 본인 채팅방 목록에서 채팅방 삭제
    public String deleteRoom(Member member, Long roomId){
        Member findMember = findMemberByAccountId(member.getAccountId());
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("채팅방을 찾을 수 없습니다."));
        ChatMember chatMember = chatMemberRepository.findByMemberAndChatRoom(findMember,chatRoom).orElseThrow(()->new ResourceNotFoundException("채팅방에 참여 중이 아닙니다."));

        chatMemberRepository.delete(chatMember);
        if(chatRoom.getChatMembers().isEmpty()){
            chatRoomRepository.delete(chatRoom);
        }
        return "채팅방 삭제 완료";
    }


}
