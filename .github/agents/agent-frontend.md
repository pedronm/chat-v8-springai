# Agent Frontend Template

## Overview
We'll be creating a single screen only, but it needs to be fully adaptive to many screens, what we'll have in this screen? A Chat screen! It'll be composed of a name at the top to identify, a + button to start a new chat, a textbox in the middle wich will be prompting out all the messages being exchanged, wich will be each in a color. 
Then the last part, the input box, that'll need to be like much like a textbox too but this one is enabled and it ill be used to send a message. The user can hit enter or hit the buton "Send"

## Core Components

### 1. React Component Example
```typescript
// components/ProjectGenerator.tsx
import React, { useState } from 'react';

export const ProjectGenerator: React.FC = () => {
  const [selectedTemplate, setSelectedTemplate] = useState<string | null>(null);
  const [projectName, setProjectName] = useState('');
  const [isGenerating, setIsGenerating] = useState(false);
  const [generatedProject, setGeneratedProject] = useState(null);

  const handleGenerate = async () => {
    setIsGenerating(true);
    try {
      const project = await agentService.generateProject(
        selectedTemplate,
        projectName
      );
      setGeneratedProject(project);
    } catch (error) {
      console.error('Generation failed:', error);
    } finally {
      setIsGenerating(false);
    }
  };

  return (
    <div className="project-generator">
      <h2>Generate New Project</h2>
      
      {/* Template Selection */}
      <section>
        <label>Select Template:</label>
        <select value={selectedTemplate || ''} onChange={e => setSelectedTemplate(e.target.value)}>
          <option value="">Choose a template...</option>
          {/* Templates rendered here */}
        </select>
      </section>

      {/* Project Name Input */}
      <section>
        <label>Project Name:</label>
        <input
          type="text"
          value={projectName}
          onChange={e => setProjectName(e.target.value)}
          placeholder="my-awesome-project"
        />
      </section>

      {/* Generate Button */}
      <button onClick={handleGenerate} disabled={!selectedTemplate || !projectName || isGenerating}>
        {isGenerating ? 'Generating...' : 'Generate Project'}
      </button>

      {/* Results Display */}
      {generatedProject && (
        <ProjectPreview project={generatedProject} />
      )}
    </div>
  );
};
```

## Available Templates

### 1. React SPA Template
- Basic React application with routing
- Component structure
- State management setup
- Styling framework
- Testing setup


## Integration Steps

### 1. Backend Integration (Spring AI)
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

### 2. API Communication
```typescript
// api/agentApi.ts
export const projectApi = {
  prompt: (config: AxiosConfig) =>
    axios(config)
      .then(r => r.json()),

  agent: (config: AxiosConfig) =>
    axios(config)
      .then(r => r.json())
};
```

## Workflow

```
User Input
    ↓
Send message to Back
    ↓
Receive message
    ↓
Show to user and update the message
```

## Features to Implement

- [ ] Title component
- [ ] Chat messages history component
- [ ] Chat send text component
- [ ] Send Button
- [ ] Service to consume the backend
- [ ] Check if theres something else needed
- [ ] Check if know what to do, else ask user or let that explained

## Error Handling

```typescript
interface GenerationError {
  code: string;
  message: string;
  details?: Record<string, any>;
}

// Common error codes
enum ErrorCodes {
  INVALID_MESSAGE = 'Mensage wasnt valid',
  TIMED_OUT = 'Chat response timed out',
}
```
