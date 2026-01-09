-- PostgreSQL initialization script for pgvector
-- This script runs automatically when the container starts

-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- Create schema for vector store
CREATE SCHEMA IF NOT EXISTS vector_store AUTHORIZATION chat_user;

-- Grant permissions
GRANT ALL PRIVILEGES ON SCHEMA vector_store TO chat_user;

-- Create table for document embeddings (Spring AI standard)
CREATE TABLE IF NOT EXISTS vector_store.documents (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    embedding vector(1536),
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for faster similarity search
CREATE INDEX IF NOT EXISTS documents_embedding_idx 
ON vector_store.documents USING ivfflat (embedding vector_cosine_ops) 
WITH (lists = 100);

-- Create table for conversation history
CREATE TABLE IF NOT EXISTS vector_store.conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create table for chat messages
CREATE TABLE IF NOT EXISTS vector_store.messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL REFERENCES vector_store.conversations(id) ON DELETE CASCADE,
    user_message TEXT NOT NULL,
    assistant_response TEXT NOT NULL,
    message_id VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES vector_store.conversations(id)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_messages_conversation_id ON vector_store.messages(conversation_id);
CREATE INDEX IF NOT EXISTS idx_conversations_user_id ON vector_store.conversations(user_id);
CREATE INDEX IF NOT EXISTS idx_documents_metadata ON vector_store.documents USING GIN(metadata);

-- Grant all permissions to chat_user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA vector_store TO chat_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA vector_store TO chat_user;
GRANT USAGE, CREATE ON SCHEMA vector_store TO chat_user;

-- Log initialization completion
SELECT 'PostgreSQL initialization completed successfully' AS status;
