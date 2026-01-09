package com.v8.pmoraes.chat_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for chat interactions.
 * Provides structured output for frontend consumption.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {
    
    private String message;
    
    private String content;
    
    private String conversationId;
    
    private String userId;
    
    private LocalDateTime timestamp;
    
    private String messageId;
    
    private boolean success;
    
    private String error;
}
