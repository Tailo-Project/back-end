package com.growith.tailo.chat.message.dto;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record ChatMessageListResponse(List<ChatMessageResponse> chatMessageResponses, Pagination pagination) {
}
