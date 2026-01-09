package com.v8.pmoraes.chat_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard error response format.
 * Provides consistent error information to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    
    private String code;
    
    private String message;
    
    private String details;
    
    private long timestamp;
}
