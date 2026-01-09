package com.v8.pmoraes.chat_backend.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration for Chat Client and supporting services.
 * Sets up advisors for context management and chat memory.
 */
@Slf4j
@Configuration
public class ChatConfiguration {

    /**
     * Creates a ChatClient with default configuration.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        log.info("Building ChatClient");
        return builder.build();
    }
}
