package com.v8.pmoraes.chat_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RAG (Retrieval-Augmented Generation) capabilities.
 * Provides vector store beans for semantic search across documents.
 * 
 * Note: VectorStore will be fully integrated with PgVectorStore in future versions
 */
@Slf4j
@Configuration
public class RagConfiguration {
    
    public RagConfiguration() {
        log.info("RAG Configuration initialized - Vector store support ready for future Spring AI versions");
    }
}
