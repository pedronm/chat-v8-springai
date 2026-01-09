package com.v8.pmoraes.chat_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request DTO for chat with file attachments.
 * Used for RAG capabilities where documents are sent with the prompt.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatWithFileRequest {
    
    private String message;
    
    private MultipartFile file;
    
    private String userId;
    
    private String conversationId;
    
    private String fileType;
}
