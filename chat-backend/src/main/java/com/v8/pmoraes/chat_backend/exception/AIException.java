package com.v8.pmoraes.chat_backend.exception;

/**
 * Custom exception for AI-related operations.
 * Thrown when there's an error with LLM interactions or RAG operations.
 */
public class AIException extends RuntimeException {
    
    private final String code;
    private final String message;
    private final Object details;
    
    public AIException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = null;
    }
    
    public AIException(String code, String message, Object details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }
    
    public AIException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.details = null;
    }
    
    public String getCode() {
        return code;
    }
    
    public Object getDetails() {
        return details;
    }
}
