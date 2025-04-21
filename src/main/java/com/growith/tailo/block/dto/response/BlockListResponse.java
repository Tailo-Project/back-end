package com.growith.tailo.block.dto.response;

import com.growith.tailo.common.dto.Pagination;

import java.util.List;

public record BlockListResponse(
        List<BlockResponse> blockResponses,
        Pagination pagination
) {
}
