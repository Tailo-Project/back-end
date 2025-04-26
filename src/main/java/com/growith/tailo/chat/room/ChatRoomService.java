//package com.growith.tailo.chat.room;
//
//import com.growith.tailo.chat.member.ChatMember;
//import com.growith.tailo.common.exception.ResourceNotFoundException;
//import com.growith.tailo.member.entity.Member;
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Service;
//@Service
//@RequiredArgsConstructor
//public class ChatRoomService {
//
//    private final RabbitTemplate rabbitTemplate;
//    private final ChatRoomRepository chatRoomRepository;
//
//    public ChatRoom getChatRoom(Member member, Long roomId) {
//        // 채팅방 조회
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
//
//        // member가 해당 채팅방에 참여한 멤버인지 확인
//        boolean isMemberInChatRoom = chatRoom.getChatMember().stream()
//                .anyMatch(chatMember -> chatMember.getMember().getId().equals(member.getId()));
//
//        if (!isMemberInChatRoom) {
//            throw new ResourceNotFoundException("해당 채팅방에 참여하지 않은 사용자입니다.");
//        }
//        // 큐가 존재하지 않으면 새로 생성
//        if (!queueExists(roomId)) {
//            createQueueForChatRoom(roomId);
//        }
//
//        return chatRoom;
//    }
//
//    private boolean queueExists(Long roomId) {
//        // 큐 존재 여부 확인 (RabbitMQ 등에서)
//        // 예시로 true/false를 반환
//        return rabbitTemplate.getTopicQueue.contains("chat.room." + roomId);
//    }
//
//    private void createQueueForChatRoom(Long roomId) {
//        // 큐 생성 로직 (예: RabbitMQ에 채팅방 큐 생성)
//        rabbitTemplate.execute(channel -> {
//            channel.queueDeclare("chat.room." + roomId, true, false, false, null);
//            return null;
//        });
//    }
//}