package com.v8.pmoraes.chat_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for chat prompts.
 * Supports both text messages and optional file uploads for RAG.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatPromptRequest {
    
    private String message;
    
    private String userId;
    
    private String conversationId;
}
