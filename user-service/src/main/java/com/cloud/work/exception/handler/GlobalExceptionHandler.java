package com.cloud.work.exception.handler;

import com.cloud.work.dto.response.ErrorResponse;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(MessageUtils.getMessage(ex.getMessage()))
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
}
