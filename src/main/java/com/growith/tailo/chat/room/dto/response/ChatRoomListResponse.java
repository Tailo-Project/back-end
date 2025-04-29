package com.growith.tailo.chat.room.dto.response;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record ChatRoomListResponse (List<ChatRoomSimpleResponse> chatRoomResponses, Pagination pagination) {
}
