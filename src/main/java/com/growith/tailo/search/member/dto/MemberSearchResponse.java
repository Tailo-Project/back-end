package com.growith.tailo.search.member.dto;

import com.growith.tailo.common.dto.Pagination;
import com.growith.tailo.search.member.docs.MemberDocument;

import java.util.List;

public record MemberSearchResponse(
        List<MemberDocument> memberDocuments,
        Pagination pagination
) {
}
