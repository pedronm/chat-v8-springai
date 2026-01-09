import React, { useState, useCallback } from 'react';
import { ChatHeader } from '../components/ChatHeader';
import { ChatMessageList } from '../components/ChatMessageList';
import { ChatInput } from '../components/ChatInput';
import { sendMessage, sendMessageWithFile, generateMessageId } from '../services/chatService';
import type { Message } from '../services/chatService';
import type { Route } from './+types/chat';

export function meta({}: Route.MetaArgs) {
  return [
    { title: 'Chat | AI Assistant' },
    { name: 'description', content: 'Chat with AI Assistant' },
  ];
}

export default function Chat() {
  const [messages, setMessages] = useState<Message[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [chatName] = useState('AI Assistant');
  const [error, setError] = useState<string | null>(null);
  const [userId] = useState<string>('default-user'); // TODO: Get from auth context
  const [conversationId, setConversationId] = useState<string | undefined>();

  const handleSendMessage = useCallback(
    async (userMessage: string, file?: File) => {
      // Add user message to the list
      const userMsg: Message = {
        id: generateMessageId(),
        content: userMessage,
        sender: 'user',
        timestamp: new Date(),
      };

      setMessages((prev) => [...prev, userMsg]);
      setIsLoading(true);
      setError(null);

      try {
        // Send message to backend with or without file
        const response = file
          ? await sendMessageWithFile(userMessage, file, userId, conversationId)
          : await sendMessage(userMessage);

        // Add assistant response to the list
        const assistantMsg: Message = {
          id: response.messageId,
          content: response.content,
          sender: 'assistant',
          timestamp: new Date(response.timestamp),
        };

        setMessages((prev) => [...prev, assistantMsg]);
      } catch (err) {
        // Handle error
        const errorMsg = err instanceof Error ? err.message : 'An error occurred';
        setError(errorMsg);

        // Add error message to the chat
        const errorMessage: Message = {
          id: generateMessageId(),
          content: `Error: ${errorMsg}`,
          sender: 'assistant',
          timestamp: new Date(),
        };

        setMessages((prev) => [...prev, errorMessage]);
      } finally {
        setIsLoading(false);
      }
    },
    []
  );

  const handleNewChat = () => {
    setMessages([]);
    setError(null);
    // You can add logic here to create a new chat session on the backend
  };

  return (
    <div className="flex flex-col h-screen bg-white dark:bg-gray-900">
      {/* Header */}
      <ChatHeader chatName={chatName} onNewChat={handleNewChat} />

      {/* Messages Container */}
      {error && (
        <div className="mx-4 my-2 p-3 bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-200 rounded-lg text-sm">
          {error}
          <button
            onClick={() => setError(null)}
            className="ml-2 font-semibold hover:underline"
          >
            Dismiss
          </button>
        </div>
      )}

      <ChatMessageList messages={messages} />

      {/* Input Area */}
      <ChatInput onSendMessage={handleSendMessage} isLoading={isLoading} />
    </div>
  );
}
