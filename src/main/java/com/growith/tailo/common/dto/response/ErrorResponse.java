package com.growith.tailo.common.dto.response;

public record ErrorResponse(Integer statusCode, String message, String details) {
}
