package com.growith.tailo.follow.dto.response;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record FollowListResponse<T>(List<T> content, Pagination pagination) {
}
