# Chat Backend - Spring AI Implementation

A powerful code assistant backend built with Spring Boot and Spring AI, featuring LLM integration, RAG (Retrieval-Augmented Generation), and agent-based reasoning.

## Features Implemented

✅ **LLM Integration**
- OpenAI GPT-4o integration via Spring AI
- Configurable model parameters (temperature, max tokens)
- Structured prompt engineering with system templates

✅ **Chat Service**
- Text-based chat prompts with context awareness
- Message validation and error handling
- Conversation management with unique IDs
- Timestamp tracking and message history

✅ **Templates & Scope Management**
- System prompt template for code assistant specialization
- Scope reduction to prevent hallucinations
- Agent-specific system templates for advanced operations
- Configurable parameters per request

✅ **Error Handling**
- Global exception handler with consistent error responses
- Standardized error codes (ErrorCode enum)
- Detailed error messages and context
- HTTP status code mapping

✅ **RAG (Retrieval-Augmented Generation)**
- File upload support (PDF, TXT, JSON, XML)
- Document extraction and chunking
- Vector store integration ready
- Semantic search capabilities
- File size validation (max 10MB)

✅ **Agent Service**
- Agent-based request processing
- Operation planning with step-by-step instructions
- Advanced reasoning capabilities
- Tool foundation for MCP integration

✅ **Configuration**
- Spring Boot 3.5.9 with Java 25
- PostgreSQL Vector Store setup
- Docker Compose for containerized deployment
- CORS support for frontend integration
- Logging configuration for debugging

## Architecture

```
chat-backend/
├── src/
│   ├── main/
│   │   ├── java/com/v8/pmoraes/chat_backend/
│   │   │   ├── config/          # Spring configurations
│   │   │   ├── chat/            # Chat service and controller
│   │   │   ├── agent/           # Agent service and controller
│   │   │   ├── rag/             # RAG service for file processing
│   │   │   ├── dto/             # Data transfer objects
│   │   │   ├── exception/       # Exception handling
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       └── application.yml  # Application configuration
│   └── test/
├── docker-compose.yml           # PostgreSQL and Redis setup
└── build.gradle                 # Gradle configuration
```

## API Endpoints

### Chat Endpoints

#### POST `/api/chat/prompt`
Process a text chat prompt.

Request:
```json
{
  "message": "Review this Python function",
  "userId": "user123",
  "conversationId": "conv456"
}
```

Response:
```json
{
  "message": "Review this Python function",
  "content": "Here's my review...",
  "conversationId": "conv456",
  "userId": "user123",
  "timestamp": "2024-01-08T10:30:00",
  "messageId": "msg789",
  "success": true
}
```

#### POST `/api/chat/prompt-with-file`
Process a chat prompt with file attachment for RAG.

Parameters:
- `message`: The user's question
- `file`: MultipartFile (PDF, TXT, JSON, XML)
- `userId`: User identifier
- `conversationId`: Optional conversation context

#### GET `/api/chat/health`
Health check endpoint.

### Agent Endpoints

#### POST `/api/agent/request`
Process an agent request with advanced reasoning.

Request:
```json
{
  "message": "Analyze this codebase and suggest refactoring",
  "userId": "user123",
  "conversationId": "conv456"
}
```

#### POST `/api/agent/plan`
Plan a complex operation with step-by-step instructions.

#### GET `/api/agent/health`
Agent service health check.

## Setup Instructions

### Prerequisites
- Java 25
- Gradle
- Docker & Docker Compose (for PostgreSQL)
- OpenAI API Key

### Local Development

1. **Clone the repository**
```bash
cd chat-backend
```

2. **Set up environment variables**
```bash
export OPENAI_API_KEY="your-api-key-here"
```

3. **Start PostgreSQL with Docker**
```bash
docker-compose up -d
```

4. **Build the project**
```bash
./gradlew build
```

5. **Run the application**
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Verify Setup

Check health endpoints:
```bash
curl http://localhost:8080/api/chat/health
curl http://localhost:8080/api/agent/health
```

## Configuration

### application.yml
Key configurations:
- `spring.ai.openai.api-key`: Your OpenAI API key
- `spring.datasource.url`: PostgreSQL connection
- `spring.jpa.hibernate.ddl-auto`: Auto schema creation
- `logging.level`: Adjust logging verbosity

### PostgreSQL Setup
Default credentials (from docker-compose.yml):
- Host: localhost:5432
- Database: chat_db
- Username: chat_user
- Password: chat_password

Access pgAdmin at `http://localhost:5050` for database management.

## Future Enhancements

### Short Term
- [ ] Implement RAG file processing in chat-with-file endpoint
- [ ] Add message persistence to database
- [ ] Implement conversation history retrieval
- [ ] Add request rate limiting

### Medium Term
- [ ] Tool integration for agents (MCP support)
- [ ] Code execution sandbox
- [ ] Advanced RAG with re-ranking
- [ ] User authentication and authorization
- [ ] Cache layer with Redis

### Long Term
- [ ] Multi-model support
- [ ] Custom fine-tuning
- [ ] Webhook notifications
- [ ] Advanced analytics and monitoring
- [ ] Streaming responses

## Error Codes

| Code | Meaning | Status |
|------|---------|--------|
| INVALID_MESSAGE | Message wasn't valid or empty | 400 |
| TIMED_OUT | Response timed out | 408 |
| LLM_ERROR | LLM processing error | 400 |
| TOKEN_LIMIT | Token limit exceeded | 400 |
| UNAUTHORIZED | Invalid API key | 401 |
| FILE_PROCESSING_ERROR | File upload/processing error | 400 |
| RAG_ERROR | RAG operation error | 400 |
| INTERNAL_ERROR | Server error | 500 |

## Testing

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests ChatServiceTest

# Test with coverage
./gradlew test jacocoTestReport
```

## Troubleshooting

**PostgreSQL Connection Failed**
- Ensure Docker container is running: `docker ps`
- Check credentials in application.yml
- Verify port 5432 is not in use

**OpenAI API Errors**
- Verify API key is set correctly
- Check API key has sufficient quota
- Ensure model name is correct (gpt-4o)

**Build Failures**
- Clean gradle cache: `./gradlew clean`
- Update dependencies: `./gradlew build --refresh-dependencies`

## Development Guidelines

### Code Style
- Use Lombok annotations to reduce boilerplate
- Add @Slf4j for logging
- Use meaningful variable names
- Add JavaDoc comments for public methods

### Adding New Features
1. Create service layer for business logic
2. Create controller for HTTP endpoints
3. Add DTOs for request/response
4. Handle exceptions with AIException
5. Add logging at INFO and DEBUG levels
6. Write unit and integration tests

### Database Migrations
- Use Hibernate's auto-schema creation (ddl-auto: update)
- Document schema changes
- Test migrations locally first

## Frontend Integration

The backend expects requests from `/api` base path. Configure frontend's API client:

```typescript
const API_BASE_URL = 'http://localhost:8080/api';
```

## Contributing

1. Create feature branch from main
2. Follow code style guidelines
3. Add tests for new features
4. Update documentation
5. Submit pull request

## License

This project is licensed under the MIT License.

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review API documentation
3. Check application logs: `logging.level.com.v8.pmoraes.chat_backend: DEBUG`
4. Open an issue on GitHub

## Changelog

### v0.0.1 (Initial Release)
- ✅ Core chat service with templates
- ✅ Error handling framework
- ✅ RAG service foundation
- ✅ Agent service with planning
- ✅ Docker setup for PostgreSQL
- ✅ Complete API endpoints
