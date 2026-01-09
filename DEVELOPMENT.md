# Chat Application - Development Guide

## Quick Start

### Backend Setup (Spring AI)

1. **Navigate to backend directory:**
   ```bash
   cd chat-backend
   ```

2. **Build the project:**
   ```bash
   ./gradlew build
   ```

3. **Run the backend:**
   ```bash
   ./gradlew bootRun
   ```
   The backend will start at `http://localhost:8080`

### Frontend Setup (React)

1. **Navigate to frontend directory:**
   ```bash
   cd chat-ui
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Create environment file:**
   ```bash
   cp .env.example .env.local
   # Edit .env.local if needed (default API URL is http://localhost:8080)
   ```

4. **Start development server:**
   ```bash
   npm run dev
   ```
   The frontend will start at `http://localhost:5173`

## Full Stack Development Workflow

### Terminal 1 - Backend
```bash
cd chat-backend
./gradlew bootRun
# Backend runs on http://localhost:8080
```

### Terminal 2 - Frontend
```bash
cd chat-ui
npm run dev
# Frontend runs on http://localhost:5173
```

Open `http://localhost:5173` in your browser to access the chat application.

## Available Commands

### Frontend Commands
- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run start` - Start production server
- `npm run typecheck` - Run TypeScript type checking

### Backend Commands
- `./gradlew build` - Build the project
- `./gradlew bootRun` - Run the application
- `./gradlew test` - Run tests

## Backend API Contract

The frontend expects the following endpoints to be available:

### POST `/chat/prompt`
Send a message and get AI response.

**Request Example:**
```bash
curl -X POST http://localhost:8080/chat/prompt \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, how are you?"}'
```

**Expected Response:**
```json
{
  "message": "I am doing well, thank you for asking!",
  "timestamp": "2024-01-08T10:30:00Z"
}
```

### POST `/chat/new` (Optional)
Create a new chat session.

**Expected Response:**
```json
{
  "chatId": "abc123xyz"
}
```

## Troubleshooting

### Frontend won't connect to backend
1. Ensure backend is running on `http://localhost:8080`
2. Check `REACT_APP_API_URL` in `.env.local`
3. Check browser console for CORS errors
4. Backend may need CORS configuration

### Port already in use
- Frontend: Change port in `vite.config.ts` or use `npm run dev -- --port 5174`
- Backend: Modify `application.properties` or set `SERVER_PORT` env variable

### TypeScript errors
Run `npm run typecheck` to verify all types are correct

### Build fails
- Clear node_modules: `rm -rf node_modules && npm install`
- Clear build cache: `rm -rf .vite dist`
- Ensure Node.js version 18+: `node --version`

## Project Files Reference

### Key Frontend Files
- [Chat Route](./chat-ui/app/routes/chat.tsx) - Main chat page
- [Chat Service](./chat-ui/app/services/chatService.ts) - API communication
- [Components](./chat-ui/app/components/) - React components
- [Styles](./chat-ui/app/app.css) - Tailwind CSS configuration

### Key Backend Files
- [Application](./chat-backend/src/main/java/com/v8/pmoraes/chat_backend/ChatBackendApplication.java)
- [Properties](./chat-backend/src/main/resources/application.properties)

## Hot Reload

Both frontend and backend support hot reload during development:
- **Frontend**: Changes to `.tsx`/`.ts`/`.css` files auto-reload the browser
- **Backend**: Changes to `.java` files may require server restart

## Building for Production

### Frontend
```bash
cd chat-ui
npm run build
# Output will be in ./build directory
```

### Backend
```bash
cd chat-backend
./gradlew build
# JAR file will be in ./build/libs
```

## Docker Support

The project includes a Dockerfile for frontend containerization:
```bash
cd chat-ui
docker build -t chat-ui .
docker run -p 3000:3000 chat-ui
```

## Debugging

### Frontend DevTools
- Open browser DevTools (F12)
- Check Network tab for API calls
- Check Console for errors and logs
- Use React Developer Tools extension for component inspection

### Backend Debugging
- Check console output from `./gradlew bootRun`
- Add logging to Spring Boot classes
- Use IDE debugger breakpoints

## Next Steps

1. Ensure both backend and frontend are running
2. Navigate to `http://localhost:5173`
3. Click on the chat page
4. Send a message to test the connection
5. Check browser console and backend logs for any errors

## Additional Resources

- [React Router Documentation](https://reactrouter.com/)
- [Tailwind CSS Documentation](https://tailwindcss.com/)
- [TypeScript Documentation](https://www.typescriptlang.org/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring AI Documentation](https://spring.io/projects/spring-ai)
