package com.v8.pmoraes.chat_backend.chat;

import com.v8.pmoraes.chat_backend.dto.ChatPromptRequest;
import com.v8.pmoraes.chat_backend.dto.ChatResponse;
import com.v8.pmoraes.chat_backend.dto.ChatWithFileRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

/**
 * Controller for chat interactions.
 * Provides endpoints for:
 * - Text-based chat prompts
 * - Chat with file uploads for RAG
 * - Agent interactions
 */
@Slf4j
@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    /**
     * Process a text chat prompt.
     * 
     * @param request ChatPromptRequest containing message, userId, conversationId
     * @return ChatResponse with LLM response
     */
    @PostMapping("/prompt")
    public ResponseEntity<ChatResponse> prompt(@RequestBody ChatPromptRequest request) {
        log.info("Received chat prompt: {}", request.getMessage());
        ChatResponse response = chatService.processPrompt(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Process chat with file upload for RAG.
     * Supports PDF, TXT, JSON, XML files.
     * 
     * @param message The user's question/prompt
     * @param file The file to process
     * @param userId The user identifier
     * @param conversationId Optional conversation context
     * @return ChatResponse with augmented context from file
     */
    @PostMapping("/prompt-with-file")
    public ResponseEntity<ChatResponse> promptWithFile(
            @RequestParam String message,
            @RequestParam MultipartFile file,
            @RequestParam String userId,
            @RequestParam(required = false) String conversationId) {
        
        log.info("Received chat prompt with file: {} ({})", file.getOriginalFilename(), file.getContentType());
        
        ChatWithFileRequest request = ChatWithFileRequest.builder()
                .message(message)
                .file(file)
                .userId(userId)
                .conversationId(conversationId)
                .fileType(file.getContentType())
                .build();
        
        try {
            // Extract content from file for RAG context
            String fileContent = chatService.extractFileContent(request);
            
            // Store content in pgvector for future retrieval (one-time operation)
            String conversationIdToUse = conversationId != null ? conversationId : UUID.randomUUID().toString();
            chatService.storeInPgVector(fileContent, file.getOriginalFilename(), conversationIdToUse);
            
            // Retrieve ONLY relevant chunks from pgvector using semantic search
            // This is more efficient than using the full file content
            String pgvectorContext = chatService.retrieveFromPgVector(message);
            
            // Use pgvector context for the RAG - it contains only relevant chunks
            // This significantly reduces token consumption compared to passing the full file
            String ragContext = !pgvectorContext.isEmpty() ? pgvectorContext : fileContent;
            
            // Create prompt request with user details
            ChatPromptRequest promptRequest = ChatPromptRequest.builder()
                    .message(message)
                    .userId(userId)
                    .conversationId(conversationIdToUse)
                    .build();
            
            // Process prompt with RAG context from pgvector (only relevant chunks)
            ChatResponse response = chatService.processPromptWithRag(promptRequest, ragContext);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ChatResponse.builder()
                            .success(false)
                            .error(e.getMessage())
                            .build());
        }
    }
    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chat service is healthy");
    }
}
