package com.v8.pmoraes.chat_backend.rag;

import com.v8.pmoraes.chat_backend.exception.AIException;
import com.v8.pmoraes.chat_backend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling RAG (Retrieval-Augmented Generation) operations.
 * Processes uploaded files, extracts text, embeds it, and stores in vector database.
 */
@Slf4j
@Service
public class RagService {
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_TYPES = {
            "application/pdf",
            "text/plain",
            "application/json",
            "application/xml",
            "text/xml"
    };
    
    // In-memory storage for RAG context (replace with VectorStore when available)
    private final List<RagDocument> documents = new ArrayList<>();
    
    /**
     * Process and store an uploaded file in the vector store.
     * 
     * @param file The file to process
     * @param userId The user who uploaded the file
     * @return Confirmation message with document count
     * @throws AIException if processing fails
     */
    public String processFile(MultipartFile file, String userId) {
        try {
            validateFile(file);
            log.info("Processing file: {} for user: {}", file.getOriginalFilename(), userId);
            
            List<Document> extractedDocs = extractDocuments(file);
            log.info("Extracted {} documents from file", extractedDocs.size());
            
            // Store with metadata
            for (Document doc : extractedDocs) {
                RagDocument ragDoc = new RagDocument(
                    doc.getText(),
                    file.getOriginalFilename(),
                    userId,
                    doc.getMetadata()
                );
                documents.add(ragDoc);
            }
            
            log.info("Successfully stored {} documents in RAG service", extractedDocs.size());
            
            return String.format("Successfully processed file '%s' with %d document segments", 
                    file.getOriginalFilename(), extractedDocs.size());
            
        } catch (AIException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing file: {}", e.getMessage(), e);
            throw new AIException(
                    ErrorCode.FILE_PROCESSING_ERROR.getCode(),
                    ErrorCode.FILE_PROCESSING_ERROR.getMessage(),
                    e.getMessage()
            );
        }
    }
    
    /**
     * Search documents using simple text matching.
     * 
     * @param query The search query
     * @return List of relevant documents
     */
    public List<RagDocument> searchDocuments(String query) {
        log.info("Searching for documents matching query: {}", query);
        String lowerQuery = query.toLowerCase();
        return documents.stream()
                .filter(doc -> doc.getContent().toLowerCase().contains(lowerQuery))
                .limit(5)
                .toList();
    }
    
    /**
     * Extract documents from an uploaded file.
     * Supports PDF, TXT, JSON, XML formats.
     * 
     * @param file The file to extract from
     * @return List of Document objects
     * @throws IOException if file reading fails
     */
    private List<Document> extractDocuments(MultipartFile file) throws IOException {
        // For now, treat all files as text
        return extractTextDocuments(file);
    }
    
    /**
     * Extract documents from text-based file.
     */
    private List<Document> extractTextDocuments(MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        
        var textSplitter = new TokenTextSplitter();
        Document doc = new Document(content);
        doc.getMetadata().put("source", file.getOriginalFilename());
        
        return textSplitter.apply(List.of(doc));
    }
    
    /**
     * Validate the uploaded file.
     * 
     * @param file The file to validate
     * @throws AIException if validation fails
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AIException(
                    ErrorCode.INVALID_MESSAGE.getCode(),
                    "File is empty or null"
            );
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AIException(
                    ErrorCode.FILE_PROCESSING_ERROR.getCode(),
                    "File size exceeds maximum allowed size of 10MB"
            );
        }
        
        String contentType = file.getContentType();
        boolean isAllowed = false;
        for (String allowed : ALLOWED_TYPES) {
            if (allowed.equals(contentType)) {
                isAllowed = true;
                break;
            }
        }
        
        if (!isAllowed) {
            throw new AIException(
                    ErrorCode.FILE_PROCESSING_ERROR.getCode(),
                    "File type not supported. Allowed types: PDF, TXT, JSON, XML"
            );
        }
    }
    
    /**
     * Inner class for RAG document storage
     */
    @lombok.Data
    public static class RagDocument {
        private String content;
        private String fileName;
        private String userId;
        private java.util.Map<String, Object> metadata;
        private long createdAt;
        
        public RagDocument(String content, String fileName, String userId, java.util.Map<String, Object> metadata) {
            this.content = content;
            this.fileName = fileName;
            this.userId = userId;
            this.metadata = metadata;
            this.createdAt = System.currentTimeMillis();
        }
    }
}
