package com.growith.tailo.search.member.dto;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record MemberSearchResponseList(
        List<MemberSearchResponse> memberSearchResponses,
        Pagination pagination
) {
}
