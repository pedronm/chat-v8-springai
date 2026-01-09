import React, { useState, useRef, useEffect } from 'react';
import { sendMessageWithFile, generateMessageId } from '../services/chatService';
import type { Message } from '../services/chatService';

interface ChatInputProps {
  onSendMessage: (message: string, file?: File) => Promise<void>;
  isLoading: boolean;
}

export const ChatInput: React.FC<ChatInputProps> = ({ onSendMessage, isLoading }) => {
  const [message, setMessage] = useState('');
  const [attachments, setAttachments] = useState<File[]>([]);
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  // Auto-resize textarea
  useEffect(() => {
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto';
      textareaRef.current.style.height = `${Math.min(textareaRef.current.scrollHeight, 150)}px`;
    }
  }, [message]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (message.trim() === '' || isLoading) {
      return;
    }

    await onSendMessage(message, attachments.length > 0 ? attachments[0] : undefined);
    setMessage('');
    setAttachments([]);
    // Reset textarea height
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto';
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSubmit(e as any);
    }
  };

  const handleAttachmentClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || []);
    // Only keep the first file for single file upload
    if (files.length > 0) {
      setAttachments([files[0]]);
    }
    // Reset file input
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  const removeAttachment = (index: number) => {
    setAttachments(attachments.filter((_, i) => i !== index));
  };

  return (
    <form onSubmit={handleSubmit} className="border-t border-gray-200 dark:border-gray-700 px-4 py-4">
      {/* Attachments display */}
      {attachments.length > 0 && (
        <div className="mb-3 flex flex-wrap gap-2">
          {attachments.map((file, index) => (
            <div
              key={index}
              className="flex items-center gap-2 px-3 py-2 bg-blue-100 dark:bg-blue-900 
                         rounded-lg text-sm text-blue-900 dark:text-blue-100"
            >
              <span>{file.name}</span>
              <button
                type="button"
                onClick={() => removeAttachment(index)}
                className="ml-1 hover:text-red-500 flex items-center"
                title="Remove file"
              >
                <img
                  src="/file-minus-alt-svgrepo-com.svg"
                  alt="Remove"
                  className="w-4 h-4"
                />
              </button>
            </div>
          ))}
        </div>
      )}

      <div className="flex gap-3 items-end">
        {/* Hidden file input */}
        <input
          ref={fileInputRef}
          type="file"
          multiple
          onChange={handleFileSelect}
          className="hidden"
          disabled={isLoading}
        />

        {/* Attachment button */}
        <button
          type="button"
          onClick={handleAttachmentClick}
          disabled={isLoading}
          className="px-4 py-3 bg-gray-300 dark:bg-gray-700 text-gray-800 dark:text-gray-200 
                     rounded-lg hover:bg-gray-400 dark:hover:bg-gray-600 
                     disabled:bg-gray-400 disabled:cursor-not-allowed font-medium 
                     transition-colors whitespace-nowrap flex items-center justify-center"
          title="Attach file"
        >
          <img
            src="/attachment-svgrepo-com.svg"
            alt="Attach"
            className="w-5 h-5"
          />
        </button>

        <textarea
          ref={textareaRef}
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Type your message... (Press Enter to send, Shift+Enter for new line)"
          className="flex-1 px-4 py-3 border border-gray-300 dark:border-gray-600 rounded-lg 
                     dark:bg-gray-800 dark:text-white focus:outline-none focus:ring-2 
                     focus:ring-blue-500 resize-none max-h-36"
          disabled={isLoading}
          rows={1}
        />
        <button
          type="submit"
          disabled={message.trim() === '' || isLoading}
          className="px-6 py-3 bg-blue-500 text-white rounded-lg hover:bg-blue-600 
                     disabled:bg-gray-400 disabled:cursor-not-allowed font-medium 
                     transition-colors whitespace-nowrap"
        >
          {isLoading ? 'Sending...' : 'Send'}
        </button>
      </div>
    </form>
  );
};
