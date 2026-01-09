# Application Architecture & Flow

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Frontend (React)                         │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    Chat Page (Route)                     │  │
│  │                                                          │  │
│  │  ┌──────────────────────────────────────────────────┐  │  │
│  │  │              ChatHeader Component                 │  │  │
│  │  │  - Title: "AI Assistant"                          │  │  │
│  │  │  - Status: "Online"                               │  │  │
│  │  │  - Button: "+ New Chat"                           │  │  │
│  │  └──────────────────────────────────────────────────┘  │  │
│  │                                                          │  │
│  │  ┌──────────────────────────────────────────────────┐  │  │
│  │  │          ChatMessageList Component               │  │  │
│  │  │  ┌──────────────────────────────────────────┐   │  │  │
│  │  │  │ ChatMessage (User - Blue, Right)        │   │  │  │
│  │  │  │ "Hello, how are you?"              10:30│   │  │  │
│  │  │  └──────────────────────────────────────────┘   │  │  │
│  │  │  ┌──────────────────────────────────────────┐   │  │  │
│  │  │  │ ChatMessage (AI - Gray, Left)           │   │  │  │
│  │  │  │ "I'm doing great, thanks!" 10:31        │   │  │  │
│  │  │  └──────────────────────────────────────────┘   │  │  │
│  │  │  [Auto-scroll to latest message]               │  │  │
│  │  └──────────────────────────────────────────────────┘  │  │
│  │                                                          │  │
│  │  ┌──────────────────────────────────────────────────┐  │  │
│  │  │           ChatInput Component                     │  │  │
│  │  │  ┌─────────────────────────────┐  ┌──────────┐  │  │  │
│  │  │  │ Textarea (auto-resize)      │  │   Send   │  │  │  │
│  │  │  │ Enter: send                 │  │  Button  │  │  │  │
│  │  │  │ Shift+Enter: new line       │  └──────────┘  │  │  │
│  │  │  └─────────────────────────────┘                │  │  │
│  │  └──────────────────────────────────────────────────┘  │  │
│  │                                                          │  │
│  │  State:                                                 │  │
│  │  - messages: Message[]                                  │  │
│  │  - isLoading: boolean                                   │  │
│  │  - error: string | null                                 │  │
│  └──────────────────────────────────────────────────────┘  │
│                        ↓ (API calls)                         │
│                   ChatService.ts                             │
└─────────────────────────────────────────────────────────────────┘
                           ↕
                      Network Layer
                    (HTTP Requests)
                           ↕
┌─────────────────────────────────────────────────────────────────┐
│                    Backend (Spring Boot)                        │
│                                                                  │
│  POST /chat/prompt                                              │
│  - Receives: { message: string }                                │
│  - Processes: AI/Spring AI processing                           │
│  - Returns: { message: string, timestamp: string }              │
│                                                                  │
│  POST /chat/new (Optional)                                      │
│  - Creates new chat session                                     │
│  - Returns: { chatId: string }                                  │
└─────────────────────────────────────────────────────────────────┘
```

## Component Hierarchy

```
root.tsx (Layout)
└── App (Router)
    ├── home.tsx (redirect to /chat)
    └── chat.tsx (Chat Page)
        ├── ChatHeader
        │   └── title, status, new chat button
        ├── ChatMessageList
        │   └── ChatMessage (multiple)
        │       └── individual message display
        └── ChatInput
            └── textarea + send button
```

## Data Flow Diagram

```
User Types Message
    ↓
Enter/Click Send
    ↓
ChatInput.onSendMessage()
    ↓
Add User Message to State
    ↓
chatService.sendMessage()
    ↓
┌─────────────────────────┐
│  Backend Processing     │
│  Spring AI / LLM        │
└─────────────────────────┘
    ↓
chatService receives Response
    ↓
Add AI Message to State
    ↓
ChatMessageList re-renders
    ↓
Auto-scroll to Latest Message
    ↓
Display to User
```

## State Management Flow

```
Chat Component
    │
    ├─ messages: Message[]
    │  │
    │  ├─ Message {
    │  │   id: string
    │  │   content: string
    │  │   sender: "user" | "assistant"
    │  │   timestamp: Date
    │  │ }
    │  └─ ChatMessageList reads and displays
    │
    ├─ isLoading: boolean
    │  │
    │  └─ ChatInput disabled while true
    │
    └─ error: string | null
       │
       └─ Error banner displays when not null
```

## Message Flow Example

```
┌─────────────────────────────────────┐
│ User Input                          │
│ "What is React?"                    │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│ ChatInput Component                 │
│ - Textarea captures input           │
│ - User presses Enter                │
│ - onSendMessage called              │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│ Chat Page Logic                     │
│ - Create user message object        │
│ - Add to messages array             │
│ - Set isLoading = true              │
│ - Call chatService.sendMessage()    │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│ ChatService                         │
│ - POST to /chat/prompt              │
│ - Send user message                 │
│ - Wait for response                 │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│ Backend Processing                  │
│ - Receive message                   │
│ - Query Spring AI                   │
│ - Generate response                 │
│ - Return with timestamp             │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│ Chat Page receives Response         │
│ - Create AI message object          │
│ - Add to messages array             │
│ - Set isLoading = false             │
│ - Trigger re-render                 │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│ ChatMessageList Component           │
│ - Receives new messages array       │
│ - Renders all messages              │
│ - ChatMessage components render     │
│ - useEffect triggers auto-scroll    │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│ User Sees in Chat                   │
│                                     │
│ ┌─────────────────────────────┐    │
│ │ What is React?      10:30   │    │
│ └─────────────────────────────┘    │
│                                     │
│ ┌─────────────────────────────┐    │
│ │ React is a JavaScript       │    │
│ │ library for building UI...  │    │
│ │                   10:31     │    │
│ └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

## Error Handling Flow

```
User sends message
    ↓
try {
  API call to backend
} catch (error) {
    ↓
    Set error state
    Add error message to chat
    Display error banner
    User can dismiss
    Can retry message
}
```

## Routing Structure

```
/                    → home.tsx
                      ↓ (redirect)
/chat                → chat.tsx (Main Chat)

Browser History:
http://localhost:5173/           [Home, redirects]
http://localhost:5173/chat       [Chat Page]
```

## Component Props Diagram

```
Chat (Page)
├── passes to ChatHeader
│   ├── chatName: string
│   └── onNewChat: () => void
├── passes to ChatMessageList
│   └── messages: Message[]
└── passes to ChatInput
    ├── onSendMessage: (msg: string) => Promise<void>
    └── isLoading: boolean
```

## Message Type Definition

```typescript
interface Message {
  id: string;              // Unique identifier
  content: string;         // Message text
  sender: 'user' | 'assistant';  // Who sent it
  timestamp: Date;         // When it was sent
}
```

## API Type Definitions

```typescript
// Request to Backend
interface SendMessageRequest {
  message: string;
}

// Response from Backend
interface ChatResponse {
  message: string;         // AI response
  timestamp: string;       // When response was created
}

// Optional: New Chat Response
interface NewChatResponse {
  chatId: string;          // New chat session ID
}
```

## Performance Optimizations

```
useEffect Hooks:
├── Auto-scroll on messages change
│   └── Smooth scroll to bottom
└── Textarea resize on input change
    └── Adjust height dynamically

useCallback Hooks:
└── handleSendMessage
    └── Prevent unnecessary re-renders

useRef Hooks:
├── endOfMessagesRef
│   └── Reference to scroll end
└── textareaRef
    └── Reference to textarea element
```

---

**Key Takeaways:**
1. User types and sends message
2. Message added to local state immediately
3. API call to backend with message
4. Backend processes with AI
5. Response received and added to state
6. UI automatically updates with auto-scroll
7. Full feedback with loading states and error handling
