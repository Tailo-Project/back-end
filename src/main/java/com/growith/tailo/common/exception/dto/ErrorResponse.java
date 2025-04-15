package com.growith.tailo.common.exception.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int statusCode;
    private final String message;
    private final String details;

    public ErrorResponse(int statusCode, String message, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }

}
