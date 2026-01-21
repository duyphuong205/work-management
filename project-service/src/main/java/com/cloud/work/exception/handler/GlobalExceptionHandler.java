package com.cloud.work.exception.handler;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.response.ErrorResponse;
import com.cloud.work.exception.BaseException;
import com.cloud.work.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(MessageUtils.getMessage(ex.getMessage()))
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error(">>>Error system: ", ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(AppConstants.RES_FAIL_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_SYSTEM_ERROR))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(AppConstants.RES_INVALID_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_INVALID_INPUT_DATA))
                .data(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
