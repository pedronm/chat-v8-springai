# Quick Reference Guide

## 📋 Project Overview

A modern, fully responsive chat interface built with React, React Router, and TypeScript. Integrated with Spring AI backend for AI-powered conversations.

## 🚀 Quick Start (3 steps)

### Step 1: Start Backend
```bash
cd chat-backend
./gradlew bootRun
```
✅ Backend running on http://localhost:8080

### Step 2: Start Frontend
```bash
cd chat-ui
npm install
npm run dev
```
✅ Frontend running on http://localhost:5173

### Step 3: Open Browser
Navigate to: `http://localhost:5173`

## 📁 File Structure

```
chat-ui/app/
├── 📂 components/
│  ├── ChatHeader.tsx       (Header with title & new chat button)
│  ├── ChatMessage.tsx      (Individual message display)
│  ├── ChatMessageList.tsx  (Messages container with auto-scroll)
│  ├── ChatInput.tsx        (Input box with send button)
│  └── index.ts             (Component exports)
├── 📂 services/
│  └── chatService.ts       (API communication)
├── 📂 routes/
│  ├── home.tsx            (Redirects to /chat)
│  └── chat.tsx            (Main chat page)
└── 📄 app.css             (Tailwind styles)
```

## 🎯 Main Components

| Component | Purpose |
|-----------|---------|
| `ChatHeader` | Title, status, new chat button |
| `ChatMessage` | Display single message with timestamp |
| `ChatMessageList` | Show all messages, auto-scroll |
| `ChatInput` | Text input with send button |
| `Chat Page` | Orchestrate all components |

## 🔌 API Endpoints Required

### POST `/chat/prompt`
Send message, get AI response

**Request:**
```json
{ "message": "Your message here" }
```

**Response:**
```json
{
  "message": "AI response",
  "timestamp": "2024-01-08T10:30:00Z"
}
```

## ⌨️ Keyboard Shortcuts

| Action | Shortcut |
|--------|----------|
| Send Message | Enter |
| New Line | Shift + Enter |
| Clear Error | Click "Dismiss" |

## 🎨 Styling Features

- ✅ Responsive (mobile, tablet, desktop)
- ✅ Dark mode support
- ✅ Color-coded messages (blue: user, gray: assistant)
- ✅ Auto-resizing textarea
- ✅ Smooth animations
- ✅ Touch-friendly on mobile

## 🔧 Common Tasks

### Build for Production
```bash
cd chat-ui
npm run build
```
Output: `./build/`

### Type Check
```bash
npm run typecheck
```

### Change API URL
Edit `.env.local`:
```
REACT_APP_API_URL=http://your-api.com
```

### Run on Different Port
```bash
npm run dev -- --port 3000
```

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| Can't connect to API | Check backend is running on 8080 |
| Port 5173 in use | `npm run dev -- --port 5174` |
| TypeScript errors | Run `npm run typecheck` |
| Components not updating | Check DevTools for React errors |
| API returning 404 | Verify `/chat/prompt` endpoint exists |

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| [IMPLEMENTATION_SUMMARY.md](./IMPLEMENTATION_SUMMARY.md) | Overview of what was built |
| [FRONTEND_IMPLEMENTATION.md](./FRONTEND_IMPLEMENTATION.md) | Detailed component docs |
| [DEVELOPMENT.md](./DEVELOPMENT.md) | Setup and development guide |

## ✨ Features

✅ Send/receive messages
✅ Auto-scroll to latest message
✅ Loading states
✅ Error handling
✅ New chat button
✅ Message timestamps
✅ Dark mode
✅ Mobile responsive
✅ Full TypeScript support

## 🔗 Dependencies

```json
{
  "react": "^19.2.3",
  "react-router": "7.10.1",
  "typescript": "^5.9.2",
  "tailwindcss": "^4.1.13"
}
```

## 📱 Browser Support

- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Mobile browsers

## 🎓 Learning Resources

- [React Documentation](https://react.dev)
- [React Router Guide](https://reactrouter.com/)
- [Tailwind CSS](https://tailwindcss.com/docs)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)

## 📞 Support

If you encounter issues:
1. Check browser console (F12)
2. Check backend logs
3. Verify API endpoints exist
4. Check network tab in DevTools
5. Review error messages in chat UI

---

**Status**: ✅ Ready to Use

The chat interface is fully implemented and ready for integration with your Spring AI backend.
