package com.growith.tailo.chat.room;

//import com.growith.tailo.chat.member.ChatMember;
import com.growith.tailo.chat.message.ChatConsumer;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    // 채팅방 생성
//    public ChatRoom


}
//    추후 도입 예정
//    private final ChatConsumer chatConsumer;
//    private final ChatRoomRepository chatRoomRepository;
//    private final AmqpAdmin amqpAdmin;
//
//    public ChatRoom getChatRoom(Member member, Long roomId) {
//        // 채팅방 조회
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new ResourceNotFoundException("채팅방을 찾을 수 없습니다."));
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
//        // 해당 큐에 대한 컨슈머 리스너 시작
//        chatConsumer.startListening(roomId);
//
//        return chatRoom;
//    }
//    // 큐 존재 여부 확인
//    private boolean queueExists(Long roomId) {
//        return amqpAdmin.getQueueProperties("chat.room." + roomId) != null;
//    }
//    // roomId를 통한 동적 큐 생성
//    private void createQueueForChatRoom(Long roomId) {
//        // 파라미터 값 : 큐이름, durable, exclusive, autoDelete, args-큐 타임아웃 TTL 설정 (30분동안 활동 없으면 삭제)
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-expires", 1800000L); // 30분 = 1800000ms
//
//        Queue queue = new Queue("chat.room." + roomId, false, false, false, args);
//        TopicExchange exchange = new TopicExchange("chat.exchange");
//        Binding binding = BindingBuilder.bind(queue).to(exchange).with("chat.room." + roomId);
//
//        try {
//            amqpAdmin.declareQueue(queue);
//            amqpAdmin.declareBinding(binding);
//        } catch (Exception e) {
//            // 예외 처리 로직 추가
//            log.error("Error creating queue or binding for roomId " + roomId, e);
//            throw new RuntimeException("채팅방 큐 생성에 실패했습니다.");
//        }
//    }
