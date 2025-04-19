package com.growith.tailo.common.dto;

public record Pagination(
        int currentPage,
        int pageSize,
        int totalPages,
        long totalItems
) {
}
