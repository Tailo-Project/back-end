package com.growith.tailo.common.handler;

import com.growith.tailo.common.dto.CommonResponse;
import com.growith.tailo.common.exception.CustomNotFoundException;
import com.growith.tailo.common.exception.CustomValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<CommonResponse<?>> handleCustomNotFoundException(CustomNotFoundException e) {
        log.error("CustomExceptionHandler CustomNotFoundException occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(801)
                .body(CommonResponse.error(801));
    }

    @ExceptionHandler(CustomValidateException.class)
    public ResponseEntity<CommonResponse<?>> handleCustomValidateException(CustomValidateException e) {
        log.error("CustomExceptionHandler CustomValidateException occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(802)
                .body(CommonResponse.error(802));
    }
}