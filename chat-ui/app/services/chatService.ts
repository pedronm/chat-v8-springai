/**
 * Chat Service - Handles communication with the backend API
 */

export interface Message {
  id: string;
  content: string;
  sender: 'user' | 'assistant';
  timestamp: Date;
}

export interface ChatResponse {
  message: string;
  content: string;
  conversationId: string;
  userId: string | null;
  timestamp: string;
  messageId: string;
  success: boolean;
  error: string | null;
}

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

/**
 * Send a message to the chat backend and get a response
 */
export const sendMessage = async (message: string): Promise<ChatResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/api/chat/prompt`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ message }),
    });

    if (!response.ok) {
      throw new Error(`API error: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error sending message:', error);
    throw new Error('Failed to send message. Please try again.');
  }
};

/**
 * Create a new chat session
 */
export const createNewChat = async (): Promise<{ chatId: string }> => {
  try {
    const response = await fetch(`${API_BASE_URL}/api/chat/new`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`API error: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error creating new chat:', error);
    throw new Error('Failed to create new chat. Please try again.');
  }
};

/**
 * Send a message with file attachment to the chat backend
 */
export const sendMessageWithFile = async (
  message: string,
  file: File,
  userId?: string,
  conversationId?: string
): Promise<ChatResponse> => {
  try {
    const formData = new FormData();
    formData.append('message', message);
    formData.append('file', file);

    // Build URL with query parameters
    const params = new URLSearchParams();
    if (userId) params.append('userId', userId);
    if (conversationId) params.append('conversationId', conversationId);
    
    const url = `${API_BASE_URL}/api/chat/prompt-with-file${params.toString() ? '?' + params.toString() : ''}`;

    const response = await fetch(url, {
      method: 'POST',
      body: formData,
      // Note: Do NOT set Content-Type header - browser will set it to multipart/form-data with boundary
    });

    if (!response.ok) {
      throw new Error(`API error: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error sending message with file:', error);
    throw new Error('Failed to send message with file. Please try again.');
  }
};

/**
 * Generate a unique message ID
 */
export const generateMessageId = (): string => {
  return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
};
