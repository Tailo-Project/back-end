package com.growith.tailo.chat.member.dto;

import com.growith.tailo.chat.member.entity.ChatMember;
import lombok.Builder;

@Builder
public record ChatMemberDto(Long memberId, String accountId, String profileImageUrl ){
    public static ChatMemberDto fromEntity(ChatMember chatMember){
        return ChatMemberDto.builder()
                .memberId(chatMember.getMember().getId())
                .accountId(chatMember.getMember().getAccountId())
                .profileImageUrl(chatMember.getMember().getProfileImageUrl())
                .build();
    }
}
