# Lipu - Codebase Analysis

## Overview
**Lipu** (formerly Timify) is an Android time-management app that uses **Gemini AI** to conversationally track how users spend their day. The AI extracts structured time-slot data from natural conversation, stores it locally, and supports analytics/insights.

**Package**: `com.streamliners.lipu`
**Min SDK**: 26 | **Target SDK**: 34

---

## Architecture

### Pattern: MVVM + Clean-ish Layers
```
ui/              → App entry, theme, navigation
feature/chat/    → Chat screen, ViewModel, UI components
feature/genAI/   → Gemini model configuration
feature/voice/   → Speech recognition
data/local/      → Room DB, DAOs, DataStore
domain/model/    → Data entities (TaskInfo, ChatHistoryItem, CustomAttribute)
di/              → Koin dependency injection
other/ext/       → Extension functions
android/helper/  → Platform helpers (TTS, DataStore, Gson adapters)
```

### Tech Stack
| Layer          | Technology                        |
|----------------|-----------------------------------|
| UI             | Jetpack Compose + Material 3      |
| Navigation     | Jetpack Navigation Compose        |
| AI             | Google Gemini 1.5 Flash           |
| DI             | Koin                              |
| DB             | Room (SQLite)                     |
| Networking     | Ktor (CIO engine)                 |
| Auth           | Firebase Auth + Google Sign-In    |
| Storage        | Firebase Storage                  |
| Preferences    | Jetpack DataStore                 |
| Serialization  | Gson                              |
| Images         | Coil                              |
| Sheets         | Google Sheets API v4              |
| Voice          | Android SpeechRecognizer + TTS    |

---

## Key Features & Implementation Details

### 1. AI-Powered Time Tracking Chat
**Files**: `GeminiModel.kt`, `ChatViewModel.kt`, `ChatScreen.kt`

- Uses **Gemini 1.5 Flash** with detailed system prompts
- Two chat modes:
  - **Normal**: Conversational time tracking - AI asks about user's day activities
  - **Insights**: SQL-generation mode - AI generates SQL queries against the TasksInfo table
- System prompt instructs AI to collect time slots (e.g., "10 AM - 12 PM : A Project")
- Special command `"give data in CSV"` triggers structured CSV output for parsing
- CSV format: `StartTime, EndTime, TaskName`

### 2. Voice Input/Output Mode
**Files**: `SpeechRecognitionButton.kt`, `VoiceMode.kt`, `TTSHelper.kt`

- **Input**: Android `RecognizerIntent` for speech-to-text
- **Output**: Android `TextToSpeech` for reading AI responses aloud
- Supports continuous voice conversation (auto-triggers next voice prompt after TTS)
- Suspend-coroutine pattern for async TTS completion

### 3. Local Data Persistence (Room DB)
**Files**: `LocalDB.kt`, `ChatHistoryDao.kt`, `TaskInfoDao.kt`, `CustomAttributeDao.kt`

- **3 entities**: ChatHistory, TasksInfo, CustomAttribute
- Chat history persisted per date + type (Normal/Insights)
- TaskInfo stores structured time slots with duration calculation
- CustomAttribute is a flexible key-value store linked to tasks
- Supports raw SQL queries for insights feature

### 4. Data Export to Google Sheets
**Files**: `TaskInfoSheetFormatInterchange.kt`, `LocalRepo.kt`

- Converts TaskInfo list to row format: `[Date, Start, End, Task Name]`
- Can parse back from sheet format to TaskInfo objects
- Supports custom attributes as additional columns
- Google Sheets API v4 integration

### 5. Insights / Analytics (Experimental)
**Files**: `InsightsChat.kt`

- Feature-flagged: `ENABLE_INSIGHTS_CHAT = false`
- AI generates SQL queries from natural language questions
- Queries executed directly against Room DB via `rawQueryAsInt()`
- Supports output types: Int, List<Int>, String, List<String>, List<TaskInfo>

### 6. Navigation
**Files**: `Screen.kt`, `NavHostGraph.kt`

- 3 screens: Chat (home), PieChart (empty), SheetSync (empty)
- PieChart and SheetSync routes exist but have no UI implementation yet

---

## Domain Models

### TaskInfo
```kotlin
@Entity("TasksInfo")
data class TaskInfo(
    id: Int,           // auto-generated
    date: String,      // "yyyy/MM/dd"
    startTime: String, // "hh:mm a"
    endTime: String,   // "hh:mm a"
    durationInMins: Int,
    name: String       // task/activity name
)
```

### ChatHistoryItem
```kotlin
@Entity("ChatHistory")
data class ChatHistoryItem(
    id: Int,
    date: String,      // "yyyy/MM/dd"
    time: Long,        // System.currentTimeMillis()
    role: String,      // "user" or "model"
    type: String,      // "Normal" or "Insights"
    message: String
)
```

### CustomAttribute
```kotlin
@Entity("CustomAttribute")
data class CustomAttribute(
    id: Int,
    taskId: Int,       // FK to TaskInfo
    key: String,
    value: String
)
```

---

## AI Prompts (Critical IP)

### Normal Chat Prompt
- Role: "time managing assistant"
- Goal: Collect user's daily activity timeline through conversation
- Output: Continuous time slots (10 AM - 12 PM : Task)
- Special CSV mode: Strict `hh:mm a` format, no extra text

### Insights Chat Prompt
- Role: SQL query generator
- Schema awareness: `TasksInfo(id, name, date, durationInMins)`
- Outputs: SQL query + output type annotation
- Hardcoded date: "Today is 2024/08/11" (bug - not dynamic)

---

## Notable Code Patterns

1. **Package naming inconsistency**: Mix of `com.streamliners.lipu` and `com.streamliners.timify` (legacy rename)
2. **Coroutine-based TTS**: `suspendCoroutine` wrapper around Android TTS callbacks
3. **Koin DI**: Single module setup with ViewModel injection
4. **Flow-based chat**: Room returns `Flow<List<ChatHistoryItem>>` for reactive UI updates
5. **Extension functions**: Gemini `Chat.send()` wrapper, `Content.asString()` helper
6. **Sealed class JSON adapter**: Custom Gson `TypeAdapterFactory` for serializing sealed classes
7. **BaseViewModel/BaseActivity**: Inherited from DroidLibs library (external)

---

## Incomplete/WIP Features
- PieChart screen (route exists, no UI)
- SheetSync screen (route exists, no UI)
- Insights chat (feature-flagged off)
- Hardcoded date in Insights prompt
- Google Sheets integration (dependency added, partial implementation)
