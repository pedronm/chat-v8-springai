# Chat Frontend - Implementation Summary

This document outlines the chat frontend implementation for the Spring AI Chat application.

## Project Structure

```
chat-ui/
├── app/
│   ├── components/
│   │   ├── ChatHeader.tsx          # Header with chat name and new chat button
│   │   ├── ChatMessage.tsx         # Individual message component
│   │   ├── ChatMessageList.tsx     # Container for all messages
│   │   ├── ChatInput.tsx           # Input box with send button
│   │   └── index.ts                # Component exports
│   ├── routes/
│   │   ├── home.tsx                # Home page (redirects to /chat)
│   │   └── chat.tsx                # Main chat page
│   ├── services/
│   │   └── chatService.ts          # API communication service
│   ├── app.css                     # Global styles with Tailwind CSS
│   └── root.tsx                    # Root layout component
├── package.json                    # Dependencies
└── .env.example                    # Environment variables template
```

## Components Overview

### 1. **ChatHeader Component**
- Displays the chat name/title
- Shows online status indicator
- Contains the "+" button to start a new chat
- Responsive design with icon and metadata

### 2. **ChatMessage Component**
- Renders individual messages
- Different styling for user (blue, right-aligned) vs assistant (gray, left-aligned)
- Displays message timestamp
- Auto-wrapping text for readability
- Mobile-responsive with max-width constraints

### 3. **ChatMessageList Component**
- Container for all chat messages
- Auto-scrolls to latest message using `useEffect` and `useRef`
- Shows empty state message when no messages
- Scrollable overflow with proper spacing

### 4. **ChatInput Component**
- Textarea that auto-resizes based on content
- Supports Enter key to send (Shift+Enter for newline)
- Send button that's disabled when:
  - Message is empty
  - Loading is in progress
- Shows loading state during message transmission
- Placeholder with helpful instructions

### 5. **Chat Page**
- Main container using flexbox for full-height layout
- Manages message state and loading state
- Handles error display with dismissible notifications
- Orchestrates communication between components
- Integrates with chat service for backend communication

## Services

### ChatService (`app/services/chatService.ts`)
- **`sendMessage(message: string)`**: Send user message to backend
  - Endpoint: `POST /chat/prompt`
  - Returns: `{ message: string, timestamp: string }`
  
- **`createNewChat()`**: Initialize a new chat session
  - Endpoint: `POST /chat/new`
  - Returns: `{ chatId: string }`

- **`generateMessageId()`**: Create unique message identifier
  - Uses timestamp + random string

## Features Implemented

✅ Responsive chat interface
✅ Message display with different styling for user/assistant
✅ Auto-scrolling to latest messages
✅ Input box with auto-resizing textarea
✅ Enter to send, Shift+Enter for newline
✅ Loading states
✅ Error handling and display
✅ New chat functionality
✅ Dark mode support (via Tailwind CSS)
✅ Mobile-first responsive design
✅ Timestamp for each message

## Styling

- **Framework**: Tailwind CSS v4
- **Color Scheme**:
  - User messages: Blue (#3b82f6)
  - Assistant messages: Gray (light/dark mode aware)
  - Buttons: Blue with hover effect
  - Errors: Red with dismissible option

- **Responsive Breakpoints**:
  - Mobile: Full width
  - Tablet: Optimized for medium screens
  - Desktop: Optimized for large screens
  - Max message width: 336px (mobile) → 576px (desktop)

## Setup and Running

### Prerequisites
- Node.js 18+ 
- npm or yarn

### Installation
```bash
cd chat-ui
npm install
```

### Environment Configuration
Create `.env.local` file (copy from `.env.example`):
```env
REACT_APP_API_URL=http://localhost:8080
```

### Development
```bash
npm run dev
```
The application will start at `http://localhost:5173`

### Build
```bash
npm run build
```

### Production
```bash
npm run start
```

## Backend Integration

The frontend expects the following endpoints from Spring AI backend:

### POST `/chat/prompt`
Sends a message to the AI and gets a response.

**Request:**
```json
{
  "message": "Your message here"
}
```

**Response:**
```json
{
  "message": "AI response here",
  "timestamp": "2024-01-08T10:30:00Z"
}
```

### POST `/chat/new`
Creates a new chat session.

**Response:**
```json
{
  "chatId": "unique-chat-id"
}
```

## Error Handling

Errors are displayed in a dismissible banner at the top of the chat. Common errors:
- Network errors: "Failed to send message. Please try again."
- API errors: HTTP status-based messages
- Validation errors: Display in error banner

Users can dismiss errors and continue chatting.

## Future Enhancements

- [ ] Chat history/persistence
- [ ] User authentication
- [ ] Message editing
- [ ] Message deletion
- [ ] Chat sessions management
- [ ] File upload support
- [ ] Rich text formatting
- [ ] Typing indicators
- [ ] Read receipts
- [ ] Search within chat history
- [ ] Export chat as PDF/text
- [ ] Settings/preferences panel

## Browser Support

- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Mobile browsers (iOS Safari, Chrome Mobile)

## Accessibility

- Semantic HTML elements
- ARIA labels where appropriate
- Keyboard navigation (Enter to send)
- Focus management for interactive elements
- Color contrast compliance (WCAG AA)

## Type Safety

- Full TypeScript support
- Type-safe message interfaces
- API response typing
- Props validation through TypeScript

## Performance Optimizations

- React.memo for message components (optional future enhancement)
- Lazy loading for long message histories (future enhancement)
- Efficient re-renders with proper dependency arrays
- Auto-scrolling uses smooth behavior (non-blocking)
