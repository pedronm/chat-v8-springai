# Agent Frontend Template

## Overview
We'll be creating a backend for the chat-ui, it is an chat to communicate with the AI i've settled in the chat-backend. Its main purpose is auxiliate within code! Or extracting from files data that may contain code, xml, json or any other type of data.

It'll cover regular prompts and also files sent, much as what a agent does! So it needs 
structured output for the data being sent off, template set in "system()" so it kepts it main purpose of being an code(developer) assitant. And also we'll be using advisors for the messages! As they'll be kept in postgrevector like database and use them by using RAG.

It should only provide necessary endpoints for receiving message and receiving message with files ! So with them work with the mentioned components. 

AS a plus, we'll be working with a '/agent' endpoint if we may have time for that, so it'll need 'tools' and some funcitonailities may require MCP

## Core Components

### 1. Controller
```java
// chat/ChatController.java
@RestController
public class ChatController {

  private final ChatClient client;
  private final Advisor advisor;

  public ChatController(ChatClient.Builder builder, advisors){
    ...
  }

  @GetMapping("/chat")
  public String chat(String msg){
    return this.chat.prompt(msg).user().system().advisor(advisor).call().content();    
  }

}  
```

### 2. Advisors
```java
   this.chatClient = builder
      .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
      .build();
```

### 3. Templates
```java
  var system = """
          Blog Post Generator Guidelines:

            1. Length & Purpose: Generate 500-word blog posts that inform and engage general audiences.

            2. Structure:
            - Introduction: Hook readers and establish the topic's relevance
            - Body: Develop 3 main points with supporting evidence and examples
            - Conclusion: Summarize key takeaways and include a call-to-action

            3. Content Requirements:
            - Include real-world applications or case studies
            - Incorporate relevant statistics or data points when appropriate
            - Explain benefits/implications clearly for non-experts

            4. Tone & Style:
            - Write in an informative yet conversational voice
            - Use accessible language while maintaining authority
            - Break up text with subheadings and short paragraphs

            5. Response Format: Deliver complete, ready-to-publish posts with a suggested title.
        """;

    return chatClient.prompt()
      .user("Generate a new article idea for my tech blog.")
      .system(system)
      .call()
      .content();
```

### 4. Rag files
```yml
  # PG Vector configuration 
  services:
  pgvector:
  image: 'pgvector/pgvector:pg16'
  environment:
  - 'POSTGRES_DB=markets'
  - 'POSTGRES_PASSWORD=secret'
  - 'POSTGRES_USER=myuser'
  labels:
  - "org.springframework.boot.service-connection=postgres"
  ports:
  - '5432'
```

```properties
  spring.application.name=rag-demo
  spring.ai.openai.api-key=${OPENAI_API_KEY}
  spring.ai.openai.chat.options.model=gpt-40

  spring.ai.vectorstore.pgvector.initialize-schema=true
```

```java
  .
  .
  .
  private final VectorStore vectorStore;
  // In our case, it should come from the user input
  @Value("classpath:/docs/article_thebeatoct2024.pdf")
  private Resource marketPDF;
  
  @Override
  public void run(String ... args) throws Exception {
    var pdfReader = new ParagraphPdfDocumentReader(marketPDF);
    TextSplitter textSplitter = new TokenTextSplitter();
    vectorStore.accept(textSplitter.apply(pdfReader.get()));
    log.info("VectorStore loaded with data!");
  .
  .
  .  
  public ModelsController(ChatClient.Builder builder, VectorStore vectorStore){
    this.chatClient = builder
      .defaultAdvisors( QuestionAnswerAdvisor.builder(vectorStore).build() )
      .build();
  }
  .
  .
  .
  @Configuration
  public class RagConfiguration {
    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){}
  }

``` 

## Available Templates

### 1. Spring AI Template
- Basic chat 
- Use of OpenAI
- Composer setup for pgvector
- Clean
- Testing setup


## Integration Steps

### 1. Frontend Integration 
```java
// Spring AI controller to handle project generation
@RestController
@RequestMapping("/chat")
public class ProjectGenerationController {
    
    @PostMapping("/prompt")
    public ResponseEntity<GeneratedProject> generateProject(
        @RequestBody ProjectGenerationRequest request
    ) {
        ...
    }
    
    @GetMapping("/agent")
    public ResponseEntity<List<TemplateConfig>> getTemplates() {
      ...
    }
}
```

## Workflow

```
Request from React
    ↓
Treat message
    ↓
Send to LLM
    ↓
Structure it in the output
```

## Features to Implement

- [ ] LLM integration
- [ ] Rag for the capability to deal with files 
- [ ] Context with advisors
- [ ] Template for reducing scope
- [ ] Composer with pgvector and its use in rag 
- [ ] Check if theres something else needed
- [ ] Check if know what to do, else ask user or let that explained

## Error Handling

```typescript
public interface AIException {
  code: string;
  message: string;
  details?: Record<string, any>;
}

// Common error codes
enum ErrorCodes {
  INVALID_MESSAGE = 'Mensage wasnt valid',
  TIMED_OUT = 'Chat response timed out',
  LM_ERROR = "Couldn't get a precise asnwer from llm",
  TOKEN_LIMIT = "Reached the max ammount of tokens",
  UNAUTHORIZED = "The key provided is invalid",
}
```
