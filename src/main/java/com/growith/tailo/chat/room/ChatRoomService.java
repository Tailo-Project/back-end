package com.growith.tailo.chat.room;

//import com.growith.tailo.chat.member.ChatMember;
import com.growith.tailo.chat.message.ChatConsumer;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    // 룸 조회
    public ChatRoomResponse getChatRoom(Member member,Long roomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new ResourceNotFoundException("해당 채팅방이 없습니다."));

        if (!chatRoom.getChatMember1().equals(member) && !chatRoom.getChatMember2().equals(member)){
            log.info("참여멤버1 {}, 참여 멤버2 {}",chatRoom.getChatMember1(), chatRoom.getChatMember2());
            throw new ResourceNotFoundException("해당 멤버가 참여중인 방이 없습니다.");
        }
           return  ChatRoomResponse.fromEntity(chatRoom);

    }
    public ChatRoomResponse createRoom(Member member, String accountId){
        Member targetMember = memberRepository.findByAccountId(accountId).orElseThrow(()-> new ResourceNotFoundException("찾는 멤버가 없습니다."));
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByChatMember1AndChatMember2OrChatMember1AndChatMember2(member, targetMember, targetMember,member);
        if(existingRoom.isPresent()){
            return getChatRoom(member,existingRoom.get().getId());
        }
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(createRoomName(member,targetMember))
                .chatMember1(member)
                .chatMember2(targetMember)
                .build();
        chatRoomRepository.save(chatRoom);
        return  ChatRoomResponse.fromEntity(chatRoom);
    }
    // 룸 이름을 생성하는 메서드
    private String createRoomName(Member member, Member targetMember) {
        // 룸 이름을 유니크하고 의미 있게 만들 수 있습니다.
        log.info("{},{} 의 채팅방", member.getAccountId(),targetMember.getAccountId());
        return member.getAccountId() + "," + targetMember.getAccountId();
    }
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
