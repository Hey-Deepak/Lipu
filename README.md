# Lipu - Learning In Public

An AI-powered Android app that helps you turn your daily coding and project work into shareable content for **Learning In Public**.

## What It Does

You're busy building across multiple Buddy Projects. Lipu makes it easy to share what you learn:

1. **Chat with AI** - Tell it what you worked on today across your projects
2. **AI generates content** - Get ready-to-share posts for Twitter, LinkedIn, or blog
3. **Track your projects** - Manage your Buddy Projects in one place
4. **Voice input** - Talk about your learnings hands-free
5. **Content history** - Review and reuse your generated content

## Tech Stack

- **Kotlin + Jetpack Compose** - Modern Android UI
- **Gemini AI (1.5 Flash)** - Content generation via conversational AI
- **Room Database** - Local storage for chat history, projects, and content
- **Firebase Auth** - User authentication
- **Koin** - Dependency injection
- **Voice Input + TTS** - Speech recognition and text-to-speech

## Setup

1. Clone the repo
2. Create `secrets.properties` in root with: `GEMINI_API_KEY="your-key-here"`
3. Open in Android Studio and run

## Content Types

Lipu can generate:
- **Tweet threads** - Concise learnings for Twitter/X
- **LinkedIn posts** - Professional learning updates
- **Blog summaries** - Longer-form learning reflections
- **TIL (Today I Learned)** - Quick bite-sized learnings

## How It Works

Tell the AI about your day:
> "Today I worked on the auth module in my e-commerce app and learned about JWT refresh tokens. Also fixed a nasty race condition in the chat feature of my messaging app."

Lipu generates:
> **Tweet**: "TIL about JWT refresh token rotation - the key insight is to invalidate the old refresh token immediately on use, not after the new one is issued. This prevents replay attacks. Building auth is like building trust - one token at a time. #LearnInPublic #AndroidDev"

## Project Structure

```
app/src/main/java/com/streamliners/lipu/
├── android/helper/     # TTS, DataStore, utilities
├── data/local/         # Room DB, DAOs, local repo
├── di/                 # Koin dependency injection
├── domain/model/       # Data models (Project, LearningEntry, ChatHistory)
├── feature/
│   ├── chat/           # Main chat screen + ViewModel
│   ├── genAI/          # Gemini model configuration
│   └── voice/          # Speech recognition
├── other/ext/          # Extension functions
└── ui/                 # App theme, navigation, MainActivity
```
