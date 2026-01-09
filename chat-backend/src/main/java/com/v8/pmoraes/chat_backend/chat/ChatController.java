package com.v8.pmoraes.chat_backend.chat;

import com.v8.pmoraes.chat_backend.dto.ChatPromptRequest;
import com.v8.pmoraes.chat_backend.dto.ChatResponse;
import com.v8.pmoraes.chat_backend.dto.ChatWithFileRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * Features implemented:
     * ✓ LLM integration - ChatService with OpenAI
     * ✓ Context with advisors - MessageChatMemoryAdvisor configured
     * ✓ Template for reducing scope - System template in ChatService
     * ✓ RAG ready - ProcessPromptWithRag method
     * TODO: Composer with pgvector and its use in rag
     * TODO: Agent endpoint with tools and MCP
     */
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
        
        // TODO: Implement RAG file processing
        // For now, process as regular prompt
        ChatPromptRequest promptRequest = ChatPromptRequest.builder()
                .message(message)
                .userId(userId)
                .conversationId(conversationId)
                .build();
        
        ChatResponse response = chatService.processPrompt(promptRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Agent endpoint for tool-based interactions.
     * Will support MCP tools and advanced reasoning.
     * 
     * @param request The agent request
     * @return Agent response with tool results
     */
    @PostMapping("/agent")
    public ResponseEntity<ChatResponse> agent(@RequestBody ChatPromptRequest request) {
        log.info("Received agent request: {}", request.getMessage());
        // TODO: Implement agent functionality with tools and MCP
        ChatResponse response = chatService.processPrompt(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chat service is healthy");
    }
}
