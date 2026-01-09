package com.v8.pmoraes.chat_backend.exception;

/**
 * Error codes for AI operations.
 * Standardized codes for consistent error handling.
 */
public enum ErrorCode {
    INVALID_MESSAGE("INVALID_MESSAGE", "Message wasn't valid or was empty"),
    TIMED_OUT("TIMED_OUT", "Chat response timed out"),
    LLM_ERROR("LLM_ERROR", "Couldn't get a precise answer from LLM"),
    TOKEN_LIMIT("TOKEN_LIMIT", "Reached the maximum amount of tokens"),
    UNAUTHORIZED("UNAUTHORIZED", "The API key provided is invalid"),
    FILE_PROCESSING_ERROR("FILE_PROCESSING_ERROR", "Error processing uploaded file"),
    RAG_ERROR("RAG_ERROR", "Error in RAG operation"),
    INTERNAL_ERROR("INTERNAL_ERROR", "Internal server error");
    
    private final String code;
    private final String message;
    
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
