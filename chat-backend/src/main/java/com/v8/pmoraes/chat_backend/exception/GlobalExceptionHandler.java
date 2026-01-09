package com.v8.pmoraes.chat_backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the application.
 * Provides consistent error responses across all endpoints.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AIException.class)
    public ResponseEntity<ErrorResponse> handleAIException(AIException ex, WebRequest request) {
        log.error("AIException occurred: {} - {}", ex.getCode(), ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .details(ex.getDetails() != null ? ex.getDetails().toString() : null)
                .timestamp(System.currentTimeMillis())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("IllegalArgumentException occurred: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.INVALID_MESSAGE.getCode())
                .message(ErrorCode.INVALID_MESSAGE.getMessage())
                .details(ex.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected exception occurred", ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_ERROR.getCode())
                .message(ErrorCode.INTERNAL_ERROR.getMessage())
                .details(ex.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
