package com.v8.pmoraes.chat_backend.chat;

import com.v8.pmoraes.chat_backend.dto.ChatPromptRequest;
import com.v8.pmoraes.chat_backend.dto.ChatResponse;
import com.v8.pmoraes.chat_backend.dto.ChatWithFileRequest;
import com.v8.pmoraes.chat_backend.exception.AIException;
import com.v8.pmoraes.chat_backend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for handling chat interactions with LLM.
 * Manages prompt processing, template application, and context handling.
 */
@Slf4j
@Service
public class ChatService {
    
    private final ChatClient chatClient;
    
    // System template for code-assistant scope
    private static final String SYSTEM_TEMPLATE = """
        You are an expert code assistant designed to help developers with:
        
        1. Code Review: Analyze and provide feedback on code quality, performance, and best practices
        2. Code Generation: Generate clean, well-documented code snippets and solutions
        3. Debugging: Help identify and fix bugs, providing explanations
        4. Architecture: Suggest architectural improvements and design patterns
        5. Documentation: Help create clear and comprehensive documentation
        
        Guidelines:
        - Always provide context and explanations for your suggestions
        - Include code examples when relevant
        - Consider best practices and industry standards
        - Be concise but thorough in your responses
        - Format code properly with appropriate syntax highlighting markers
        
        When dealing with files:
        - Extract and understand the code structure
        - Identify potential issues and improvements
        - Maintain the original intent while suggesting enhancements
        
        Response Format:
        - Use markdown for formatting
        - Include code blocks with language specification
        - Structure complex answers with clear sections
        """;
    
    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    /**
     * Process a chat prompt and return a response.
     * 
     * @param request The chat request containing the message
     * @return ChatResponse with the LLM's answer
     * @throws AIException if processing fails
     */
    public ChatResponse processPrompt(ChatPromptRequest request) {
        try {
            validateRequest(request);
            log.info("Processing chat prompt for user: {}", request.getUserId());
            
            // Call LLM with template and context
            String response = chatClient.prompt()
                    .system(SYSTEM_TEMPLATE)
                    .user(request.getMessage())
                    .call()
                    .content();
            
            return buildChatResponse(request, response, true);
            
        } catch (AIException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing prompt: {}", e.getMessage(), e);
            throw new AIException(
                    ErrorCode.LLM_ERROR.getCode(),
                    ErrorCode.LLM_ERROR.getMessage(),
                    e.getMessage()
            );
        }
    }
    
    /**
     * Process a prompt with RAG context from uploaded files.
     * 
     * @param request The chat request
     * @param ragContext Additional context from RAG
     * @return ChatResponse with the LLM's answer
     */
    public ChatResponse processPromptWithRag(ChatPromptRequest request, String ragContext) {
        try {
            validateRequest(request);
            log.info("Processing chat prompt with RAG for user: {}", request.getUserId());
            
            String augmentedPrompt = String.format("""
                    Based on the following document context, please answer the question:
                    
                    CONTEXT:
                    %s
                    
                    QUESTION:
                    %s
                    """, ragContext, request.getMessage());
            
            String response = chatClient.prompt()
                    .system(SYSTEM_TEMPLATE)
                    .user(augmentedPrompt)
                    .call()
                    .content();
            
            return buildChatResponse(request, response, true);
            
        } catch (AIException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing prompt with RAG: {}", e.getMessage(), e);
            throw new AIException(
                    ErrorCode.RAG_ERROR.getCode(),
                    ErrorCode.RAG_ERROR.getMessage(),
                    e.getMessage()
            );
        }
    }
    
    /**
     * Validate the incoming request.
     * 
     * @param request The request to validate
     * @throws AIException if validation fails
     */
    private void validateRequest(ChatPromptRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new AIException(
                    ErrorCode.INVALID_MESSAGE.getCode(),
                    ErrorCode.INVALID_MESSAGE.getMessage()
            );
        }
    }
    
    /**
     * Build a ChatResponse from the request and LLM response.
     * 
     * @param request Original request
     * @param content Response content from LLM
     * @param success Whether processing was successful
     * @return ChatResponse object
     */
    private ChatResponse buildChatResponse(ChatPromptRequest request, String content, boolean success) {
        return ChatResponse.builder()
                .message(request.getMessage())
                .content(content)
                .conversationId(request.getConversationId() != null ? request.getConversationId() : UUID.randomUUID().toString())
                .userId(request.getUserId())
                .timestamp(LocalDateTime.now())
                .messageId(UUID.randomUUID().toString())
                .success(success)
                .build();
    }

    /**
     * Extract content from an uploaded file for RAG processing.
     * Supports: TXT, JSON, XML, and other text-based formats
     * 
     * @param request The chat request with file
     * @return Extracted file content as String
     * @throws AIException if file extraction fails
     */
    public String extractFileContent(ChatWithFileRequest request) {
        try {
            String originalFilename = request.getFile().getOriginalFilename();
            String contentType = request.getFileType();
            
            log.info("Extracting content from file: {} ({})", originalFilename, contentType);
            
            // Convert file bytes to String
            String fileContent = new String(request.getFile().getBytes());
            
            if (fileContent.trim().isEmpty()) {
                throw new AIException(
                        ErrorCode.INVALID_MESSAGE.getCode(),
                        "File is empty or could not be read"
                );
            }
            
            log.info("Successfully extracted {} characters from file", fileContent.length());
            return fileContent;
            
        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage(), e);
            throw new AIException(
                    ErrorCode.LLM_ERROR.getCode(),
                    "Failed to read uploaded file: " + e.getMessage()
            );
        } catch (AIException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error extracting file content: {}", e.getMessage(), e);
            throw new AIException(
                    ErrorCode.LLM_ERROR.getCode(),
                    "Failed to process uploaded file"
            );
        }
    }
}
