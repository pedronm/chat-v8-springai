# Setup Instructions - Step by Step

## Prerequisites

- **Node.js** 18+ ([Download](https://nodejs.org/))
- **Java JDK** 11+ ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Git** (for cloning/version control)
- **Terminal/Command Prompt**
- A modern web browser (Chrome, Firefox, Safari, Edge)

## Step-by-Step Setup

### Phase 1: Verify Installation

#### 1.1 Check Node.js Installation
```bash
node --version  # Should show v18+
npm --version   # Should show 10+
```

#### 1.2 Check Java Installation
```bash
java -version   # Should show Java 11+
```

### Phase 2: Backend Setup

#### 2.1 Navigate to Backend Directory
```bash
cd /path/to/chat-v8-springai/chat-backend
```

#### 2.2 Build the Project
```bash
# On Windows
gradlew.bat build

# On macOS/Linux
./gradlew build
```
⏳ This may take 1-2 minutes on first run (downloading dependencies)

#### 2.3 Run the Backend
```bash
# On Windows
gradlew.bat bootRun

# On macOS/Linux
./gradlew bootRun
```

**Success Indicators:**
```
Started ChatBackendApplication in X seconds
Listening on port 8080
```

✅ Leave this terminal running. Backend is ready at `http://localhost:8080`

### Phase 3: Frontend Setup

#### 3.1 Open New Terminal Window
Do NOT close the backend terminal. Open a NEW terminal.

#### 3.2 Navigate to Frontend Directory
```bash
cd /path/to/chat-v8-springai/chat-ui
```

#### 3.3 Install Dependencies
```bash
npm install
```
⏳ This may take 2-3 minutes on first run

#### 3.4 Create Environment Configuration
```bash
# Create .env.local file
cp .env.example .env.local
```

#### 3.5 Verify Environment File
Open `.env.local` in your editor. It should contain:
```env
REACT_APP_API_URL=http://localhost:8080
```

#### 3.6 Start Development Server
```bash
npm run dev
```

**Success Indicators:**
```
  VITE v7.1.7  ready in XXX ms

  ➜  Local:   http://localhost:5173/
  ➜  press h + enter to show help
```

✅ Frontend is ready

### Phase 4: Test the Application

#### 4.1 Open Browser
Navigate to: `http://localhost:5173`

#### 4.2 Verify Interface Loads
You should see:
- Header with "AI Assistant" and "+" button
- Empty chat area with message "Start a conversation..."
- Text input box at bottom with "Send" button

#### 4.3 Test Message Sending
1. Type: "Hello, how are you?"
2. Press `Enter` or click `Send`
3. Message appears in chat (blue bubble, right side)
4. After a moment, AI response appears (gray bubble, left side)

**Success**: You see both your message and the AI response!

#### 4.4 Test Keyboard Shortcuts
```
Enter          → Send message
Shift + Enter  → New line in message
Type "..."     → Auto-resize textarea
```

### Phase 5: Verify All Systems

#### 5.1 Check Backend Terminal
You should see log entries:
```
[http-nio-8080-exec-1] ... "POST /chat/prompt HTTP/1.1" 200
```

#### 5.2 Check Frontend Terminal
No error messages, should show:
```
✓ [200] GET /
✓ [200] GET /chat
```

#### 5.3 Check Browser Console
Open DevTools (F12 or Right Click → Inspect):
- **Console tab**: No red errors
- **Network tab**: Requests to `/chat/prompt` should show 200 status
- **Application tab**: No service worker errors

## Troubleshooting Guide

### Issue: Backend won't start

**Error**: `Port 8080 already in use`
```bash
# Solution: Kill process using port 8080
# On Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# On macOS/Linux:
lsof -i :8080
kill -9 <PID>
```

**Error**: `Java not found`
```bash
# Solution: Install Java JDK
# Windows: Download from Oracle
# macOS: brew install java
# Linux: sudo apt install default-jdk
```

**Error**: `Gradle wrapper not found`
```bash
# Solution: Check you're in the right directory
cd chat-backend  # Must be in this directory
```

### Issue: Frontend won't start

**Error**: `Port 5173 already in use`
```bash
# Solution: Use different port
npm run dev -- --port 5174
```

**Error**: `Node modules not installed`
```bash
# Solution: Install dependencies
rm -rf node_modules
npm install
```

**Error**: `EACCES: permission denied`
```bash
# Solution: Fix npm permissions
npm install -g npm@latest
```

### Issue: Can't send messages

**Error**: `Failed to send message. Please try again.`

**Solution Steps:**
1. Check backend is running (Terminal 1)
   - Should show: `Started ChatBackendApplication`
   - Should show port 8080 is listening

2. Check API URL in `.env.local`
   - Should be: `REACT_APP_API_URL=http://localhost:8080`

3. Check browser console for errors (F12)
   - Look for network errors
   - Check if requests reach backend

4. Check backend logs
   - Should show: `"POST /chat/prompt HTTP/1.1" 200`

5. Verify endpoint exists
   - Backend must have `/chat/prompt` endpoint
   - Must accept POST requests
   - Must return `{ message: string }`

### Issue: Messages show as errors

**Error**: `Error: Failed to send message`

**Check:**
1. Backend `/chat/prompt` endpoint is implemented
2. Response format is: `{ "message": "...", "timestamp": "..." }`
3. CORS is configured on backend (if needed)
4. No validation errors in request

### Issue: Dark mode not working

**Solution:**
Check if your system is in dark mode:
- Windows: Settings → Personalization → Colors
- macOS: System Preferences → General → Appearance
- Linux: Depends on desktop environment

Or manually toggle in browser DevTools:
- Open DevTools (F12)
- Find `<html>` element
- Add class `dark` to test dark mode

### Issue: TypeScript errors

**Solution:**
```bash
npm run typecheck
```

This will show all type errors. Common fixes:
- Check imports are correct
- Verify API response types match
- Update TypeScript version: `npm install -g typescript@latest`

## Health Check Checklist

Before declaring success, verify:

- [ ] Backend terminal shows "Started ChatBackendApplication"
- [ ] Backend terminal shows "Listening on port 8080"
- [ ] Frontend terminal shows "ready in XXX ms"
- [ ] Frontend shows URL: "http://localhost:5173/"
- [ ] Browser loads chat interface
- [ ] No red errors in browser console
- [ ] Can type message in input box
- [ ] Can send message with Enter
- [ ] User message appears in blue (right)
- [ ] AI response appears in gray (left)
- [ ] Timestamps show on messages
- [ ] Can click "+" button (new chat)
- [ ] Can dismiss errors if they occur

## Next Steps After Setup

### 1. Customize the Application
- Edit chat name in `chat.tsx`
- Change colors in component styles
- Adjust Tailwind CSS in `app.css`

### 2. Implement Backend Endpoints
- Create `/chat/prompt` endpoint
- Create `/chat/new` endpoint
- Integrate with Spring AI
- Test with Postman or curl

### 3. Add Features
- Chat history persistence
- User authentication
- Multiple chat sessions
- Message editing
- File uploads

### 4. Deploy Application
- Build frontend: `npm run build`
- Build backend: `./gradlew build`
- Deploy to server or cloud platform
- Configure environment variables for production

## Common Commands Reference

```bash
# Frontend
npm install        # Install dependencies
npm run dev        # Start development
npm run build      # Build for production
npm run start      # Start production server
npm run typecheck  # Check TypeScript

# Backend
./gradlew build    # Build project
./gradlew bootRun  # Run application
./gradlew test     # Run tests
./gradlew clean    # Clean build files
```

## Useful Ports

| Service | Port | URL |
|---------|------|-----|
| Frontend | 5173 | http://localhost:5173 |
| Backend | 8080 | http://localhost:8080 |
| DevTools | - | F12 in browser |

## Documentation Files to Read

1. **QUICK_REFERENCE.md** - Fast lookup guide (5 min read)
2. **ARCHITECTURE.md** - System design and flows (10 min read)
3. **IMPLEMENTATION_SUMMARY.md** - What was built (5 min read)
4. **FRONTEND_IMPLEMENTATION.md** - Component details (15 min read)
5. **DEVELOPMENT.md** - Advanced setup (10 min read)

## Getting Help

### Check These First
1. Browser console (F12 → Console tab)
2. Backend terminal output
3. Frontend terminal output
4. Error messages in chat UI

### Information to Gather
1. Exact error message
2. Operating system (Windows/Mac/Linux)
3. Node.js and Java versions
4. Steps to reproduce
5. Screenshots of error

### Resources
- [React Documentation](https://react.dev)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Tailwind CSS Docs](https://tailwindcss.com/docs)

## Success Indicators Summary

### ✅ Everything Working
- Both terminals running without errors
- Browser shows chat interface
- Can send/receive messages
- No red errors in console
- Backend processes requests (see logs)

### ⚠️ Partial Setup
- One service running but not both
- Interface loads but messages don't send
- Errors in console but app works

### ❌ Failed Setup
- Dependencies won't install
- Services won't start
- Interface doesn't load
- Errors prevent any functionality

---

**Expected Setup Time**: 10-15 minutes total

Once you see messages flowing in the chat, you're done! 🎉
