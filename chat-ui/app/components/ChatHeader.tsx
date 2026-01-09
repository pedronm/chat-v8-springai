import React from 'react';
import springAiLogo from '../../assets/spring_ai_logo_with_text.svg';
import chatIcon from '../../assets/java-1.svg';

interface ChatHeaderProps {
  chatName: string;
  onNewChat: () => void;
}

export const ChatHeader: React.FC<ChatHeaderProps> = ({ chatName, onNewChat }) => {
  return (
    <header className="border-b border-gray-200 dark:border-gray-700 px-4 py-4 flex items-center justify-between bg-white dark:bg-gray-900">
      <div className="flex items-center gap-4">
        <img src={springAiLogo} alt="Spring AI" className="h-8" />
        <div className="flex items-center gap-3">
          <div>
            <h1 className="text-lg font-semibold text-gray-900 dark:text-white">
              {chatName || 'Chat'}
            </h1>
            <p className="text-xs text-gray-500 dark:text-gray-400">Online</p>
          </div>
        </div>
      </div>
      <button
        onClick={onNewChat}
        className="flex items-center justify-center w-10 h-10 rounded-full 
                   bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700
                   text-gray-700 dark:text-gray-300 font-bold text-xl
                   transition-colors cursor-pointer"
        title="Start a new chat"
      >
        +
      </button>
    </header>
  );
};
