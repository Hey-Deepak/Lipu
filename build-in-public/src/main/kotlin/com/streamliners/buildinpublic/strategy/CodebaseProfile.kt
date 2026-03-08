package com.streamliners.buildinpublic.strategy

import com.streamliners.buildinpublic.model.DiffCategory

object CodebaseProfile {

    const val PACKAGE_NAME = "com.streamliners.lipu"
    const val LEGACY_PACKAGE = "com.streamliners.timify"
    const val MIN_SDK = 26
    const val TARGET_SDK = 34

    enum class TechStack(val displayName: String, val purpose: String) {
        JETPACK_COMPOSE("Jetpack Compose + Material 3", "UI"),
        NAVIGATION_COMPOSE("Jetpack Navigation Compose", "Navigation"),
        GEMINI("Google Gemini 1.5 Flash", "AI / Generative AI"),
        KOIN("Koin", "Dependency Injection"),
        ROOM("Room (SQLite)", "Local Database"),
        KTOR("Ktor (CIO engine)", "Networking"),
        FIREBASE_AUTH("Firebase Auth + Google Sign-In", "Authentication"),
        FIREBASE_STORAGE("Firebase Storage", "Cloud Storage"),
        DATASTORE("Jetpack DataStore", "Preferences"),
        GSON("Gson", "Serialization"),
        COIL("Coil", "Image Loading"),
        GOOGLE_SHEETS("Google Sheets API v4", "Data Export"),
        SPEECH_RECOGNIZER("Android SpeechRecognizer + TTS", "Voice I/O"),
    }

    data class Feature(
        val name: String,
        val description: String,
        val keyFiles: List<String>,
        val techUsed: List<TechStack>,
        val postAngle: String
    )

    val features = listOf(
        Feature(
            name = "AI Chat Time Tracking",
            description = "Gemini AI conversationally collects user's daily time slots through natural chat",
            keyFiles = listOf("GeminiModel.kt", "ChatViewModel.kt", "ChatScreen.kt"),
            techUsed = listOf(TechStack.GEMINI, TechStack.JETPACK_COMPOSE),
            postAngle = "AI-powered productivity, prompt engineering for structured output"
        ),
        Feature(
            name = "Voice Input/Output",
            description = "Speech-to-text input and TTS response for hands-free time tracking",
            keyFiles = listOf("SpeechRecognitionButton.kt", "VoiceMode.kt", "TTSHelper.kt"),
            techUsed = listOf(TechStack.SPEECH_RECOGNIZER),
            postAngle = "Voice UX, coroutine wrappers for Android callbacks"
        ),
        Feature(
            name = "Local Data Persistence",
            description = "Room DB with 3 entities: ChatHistory, TasksInfo, CustomAttribute",
            keyFiles = listOf("LocalDB.kt", "ChatHistoryDao.kt", "TaskInfoDao.kt", "CustomAttributeDao.kt"),
            techUsed = listOf(TechStack.ROOM),
            postAngle = "Room + Flow for reactive chat, schema design decisions"
        ),
        Feature(
            name = "Google Sheets Export",
            description = "Export structured time data to Google Sheets",
            keyFiles = listOf("TaskInfoSheetFormatInterchange.kt", "LocalRepo.kt"),
            techUsed = listOf(TechStack.GOOGLE_SHEETS),
            postAngle = "Data interoperability, format conversion"
        ),
        Feature(
            name = "Insights Chat (Experimental)",
            description = "Natural language questions converted to SQL queries by AI, executed on Room DB",
            keyFiles = listOf("InsightsChat.kt"),
            techUsed = listOf(TechStack.GEMINI, TechStack.ROOM),
            postAngle = "NL-to-SQL is compelling AI content, feature flag strategy"
        )
    )

    val highValueWatchFiles: Map<String, Pair<DiffCategory, String>> = mapOf(
        "GeminiModel.kt" to (DiffCategory.AI_PROMPT to "Any prompt or model config change is gold content"),
        "ChatViewModel.kt" to (DiffCategory.NEW_FEATURE to "Core AI interaction loop"),
        "InsightsChat.kt" to (DiffCategory.AI_PROMPT to "Natural language to SQL is compelling content"),
        "Screen.kt" to (DiffCategory.NEW_FEATURE to "New screens = new features"),
        "TTSHelper.kt" to (DiffCategory.NEW_FEATURE to "Voice features are demo-friendly"),
    )

    val wipFeatures = listOf(
        "PieChart screen (route exists, no UI)",
        "SheetSync screen (route exists, no UI)",
        "Insights chat (feature-flagged off)",
        "Hardcoded date in Insights prompt (not dynamic)",
        "Google Sheets integration (partial implementation)"
    )

    val notablePatterns = listOf(
        "Package naming inconsistency: mix of 'lipu' and 'timify' (legacy rename)",
        "Coroutine-based TTS: suspendCoroutine wrapper around Android TTS callbacks",
        "Koin DI: single module setup with ViewModel injection",
        "Flow-based chat: Room returns Flow<List<ChatHistoryItem>> for reactive UI",
        "Extension functions: Gemini Chat.send() wrapper, Content.asString() helper",
        "Sealed class JSON adapter: custom Gson TypeAdapterFactory for sealed classes",
        "CSV parsing: AI outputs structured CSV, parsed in saveTaskInfoToLocal()"
    )
}
