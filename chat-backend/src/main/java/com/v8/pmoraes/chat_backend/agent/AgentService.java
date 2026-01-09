package com.v8.pmoraes.chat_backend.agent;

import com.v8.pmoraes.chat_backend.dto.ChatPromptRequest;
import com.v8.pmoraes.chat_backend.dto.ChatResponse;
import com.v8.pmoraes.chat_backend.exception.AIException;
import com.v8.pmoraes.chat_backend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for handling agent-based interactions.
 * Agents have access to tools and can perform complex reasoning.
 * 
 * Future capabilities:
 * - Tool invocation and MCP (Model Context Protocol) integration
 * - Multi-step reasoning and planning
 * - File system access control
 * - Code execution sandbox
 */
@Slf4j
@Service
public class AgentService {
    
    private final ChatClient chatClient;
    
    // System template for agent operations
    private static final String AGENT_SYSTEM_TEMPLATE = """
        You are an advanced code development agent with access to specialized tools.
        
        Your capabilities include:
        1. Code Analysis: Deep analysis of code structure, dependencies, and quality metrics
        2. Multi-file Operations: Work with multiple files in a project
        3. Testing: Generate and suggest comprehensive test cases
        4. Refactoring: Plan and execute code refactoring operations
        5. Documentation: Generate comprehensive documentation and API specs
        
        Guidelines:
        - Always explain your reasoning and steps
        - Consider security implications of any operations
        - Provide clear, actionable recommendations
        - Break complex tasks into smaller steps
        - Validate assumptions before proceeding
        
        Available Tools (to be implemented):
        - read_file: Read contents of specified files
        - search_code: Search for patterns in codebase
        - execute_analysis: Run code analysis tools
        - suggest_refactoring: Provide refactoring suggestions
        
        Remember to:
        - Request file access before reading
        - Explain the impact of suggested changes
        - Provide implementation examples
        - Document all recommendations
        """;
    
    public AgentService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    /**
     * Process an agent request with advanced reasoning.
     * 
     * @param request The agent request
     * @return ChatResponse with agent's analysis and recommendations
     * @throws AIException if processing fails
     */
    public ChatResponse processAgentRequest(ChatPromptRequest request) {
        try {
            validateRequest(request);
            log.info("Processing agent request for user: {}", request.getUserId());
            
            String response = chatClient.prompt()
                    .system(AGENT_SYSTEM_TEMPLATE)
                    .user(request.getMessage())
                    .call()
                    .content();
            
            return buildAgentResponse(request, response, true);
            
        } catch (AIException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing agent request: {}", e.getMessage(), e);
            throw new AIException(
                    ErrorCode.LLM_ERROR.getCode(),
                    ErrorCode.LLM_ERROR.getMessage(),
                    e.getMessage()
            );
        }
    }
    
    /**
     * Plan a complex operation with multiple steps.
     * 
     * @param request The operation request
     * @return ChatResponse with step-by-step plan
     */
    public ChatResponse planOperation(ChatPromptRequest request) {
        try {
            validateRequest(request);
            log.info("Planning operation for user: {}", request.getUserId());
            
            String planPrompt = String.format("""
                    Please create a detailed step-by-step plan for the following operation:
                    
                    %s
                    
                    For each step, include:
                    1. What needs to be done
                    2. Why it's necessary
                    3. Expected outcome
                    4. Potential risks or considerations
                    """, request.getMessage());
            
            String response = chatClient.prompt()
                    .system(AGENT_SYSTEM_TEMPLATE)
                    .user(planPrompt)
                    .call()
                    .content();
            
            return buildAgentResponse(request, response, true);
            
        } catch (AIException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error planning operation: {}", e.getMessage(), e);
            throw new AIException(
                    ErrorCode.LLM_ERROR.getCode(),
                    "Failed to create operation plan",
                    e.getMessage()
            );
        }
    }
    
    /**
     * Validate the incoming request.
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
     * Build an agent response.
     */
    private ChatResponse buildAgentResponse(ChatPromptRequest request, String content, boolean success) {
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
}
