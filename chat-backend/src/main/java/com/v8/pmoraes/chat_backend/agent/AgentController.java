package com.v8.pmoraes.chat_backend.agent;

import com.v8.pmoraes.chat_backend.dto.ChatPromptRequest;
import com.v8.pmoraes.chat_backend.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for agent-based interactions.
 * Provides endpoints for advanced reasoning and tool-based operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "*")
public class AgentController {
    
    private final AgentService agentService;
    
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }
    
    /**
     * Process an agent request with advanced reasoning.
     * 
     * @param request The agent request
     * @return ChatResponse with agent's analysis
     */
    @PostMapping("/request")
    public ResponseEntity<ChatResponse> processAgentRequest(@RequestBody ChatPromptRequest request) {
        log.info("Processing agent request");
        ChatResponse response = agentService.processAgentRequest(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Plan a complex operation with step-by-step instructions.
     * 
     * @param request The operation to plan
     * @return ChatResponse with detailed plan
     */
    @PostMapping("/plan")
    public ResponseEntity<ChatResponse> planOperation(@RequestBody ChatPromptRequest request) {
        log.info("Planning operation");
        ChatResponse response = agentService.planOperation(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Health check for agent service.
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Agent service is operational");
    }
}
