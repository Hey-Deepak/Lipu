package com.streamliners.buildinpublic.model

/**
 * Rules for classifying file paths to diff categories.
 * Based on Lipu's actual project structure.
 */
object FileClassification {

    /**
     * Maps a file path to its most likely diff category.
     */
    fun classify(filePath: String): DiffCategory? {
        return rules.firstOrNull { (pattern, _) ->
            pattern.containsMatchIn(filePath)
        }?.second
    }

    /**
     * Classification rules ordered by specificity (most specific first).
     */
    private val rules: List<Pair<Regex, DiffCategory>> = listOf(
        // AI/Prompt Engineering
        Regex("genAI/") to DiffCategory.AI_PROMPT,
        Regex("GeminiModel") to DiffCategory.AI_PROMPT,

        // Data Models & Persistence
        Regex("domain/model/") to DiffCategory.DATA_MODEL,
        Regex("data/local/dao/") to DiffCategory.DATA_MODEL,
        Regex("data/local/LocalDB") to DiffCategory.DATA_MODEL,

        // Architecture
        Regex("di/") to DiffCategory.ARCHITECTURE,
        Regex("NavHostGraph") to DiffCategory.ARCHITECTURE,
        Regex("Screen\\.kt") to DiffCategory.ARCHITECTURE,

        // UI/UX
        Regex("feature/.*/comp/") to DiffCategory.UI_UX,
        Regex("ui/theme/") to DiffCategory.UI_UX,

        // Feature (broader - chat, voice, etc.)
        Regex("feature/chat/ChatViewModel") to DiffCategory.NEW_FEATURE,
        Regex("feature/chat/ChatScreen") to DiffCategory.NEW_FEATURE,
        Regex("feature/voice/") to DiffCategory.NEW_FEATURE,
        Regex("feature/") to DiffCategory.NEW_FEATURE,

        // Helpers / Android
        Regex("android/helper/") to DiffCategory.NEW_FEATURE,
        Regex("other/ext/") to DiffCategory.NEW_FEATURE,

        // Dependencies
        Regex("\\.gradle") to DiffCategory.DEPENDENCY,
        Regex("libs\\.versions\\.toml") to DiffCategory.DEPENDENCY,

        // Config
        Regex("AndroidManifest") to DiffCategory.CONFIG,
        Regex("proguard") to DiffCategory.CONFIG,
        Regex("secrets\\.properties") to DiffCategory.CONFIG,
    )

    /**
     * Keywords in diff content that signal specific categories,
     * regardless of the file they appear in.
     */
    val contentSignals: Map<DiffCategory, List<Regex>> = mapOf(
        DiffCategory.AI_PROMPT to listOf(
            Regex("SYSTEM_INSTRUCTION"),
            Regex("systemInstruction"),
            Regex("modelName\\s*="),
            Regex("temperature\\s*="),
            Regex("topK\\s*="),
            Regex("topP\\s*="),
            Regex("maxOutputTokens"),
            Regex("responseMimeType"),
        ),
        DiffCategory.NEW_FEATURE to listOf(
            Regex("@Composable"),
            Regex("class\\s+\\w+ViewModel"),
            Regex("fun\\s+\\w+Screen"),
            Regex("@Entity"),
            Regex("@Dao"),
        ),
        DiffCategory.BUG_FIX to listOf(
            Regex("\\?\\."),       // null-safe call
            Regex("\\?:"),         // elvis operator
            Regex("try\\s*\\{"),   // try-catch
            Regex("catch\\s*\\("), // catch block
            Regex("runCatching"),
        ),
        DiffCategory.UI_UX to listOf(
            Regex("@Composable"),
            Regex("Modifier\\."),
            Regex("MaterialTheme"),
            Regex("AnimatedContent"),
            Regex("animate\\w+As"),
        ),
        DiffCategory.CONFIG to listOf(
            Regex("ENABLE_"),      // feature flags
            Regex("BuildConfig"),
            Regex("buildConfigField"),
        ),
    )
}
