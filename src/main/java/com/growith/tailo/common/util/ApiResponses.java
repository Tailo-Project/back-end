package com.growith.tailo.common.util;

import com.growith.tailo.common.dto.response.ApiResponse;

public class ApiResponses {

    public static <T> ApiResponse<Void> success() {
        return new ApiResponse<>(200, "success", null);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<Void> created() {
        return new ApiResponse<>(201, "created", null);
    }

    public static <T> ApiResponse<T> created(String message) {
        return new ApiResponse<>(201, message, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "created", data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data);
    }

}