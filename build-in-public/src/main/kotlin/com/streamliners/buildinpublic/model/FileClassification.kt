package com.streamliners.buildinpublic.model

object FileClassification {

    fun classify(filePath: String): DiffCategory? {
        return rules.firstOrNull { (pattern, _) ->
            pattern.containsMatchIn(filePath)
        }?.second
    }

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

        // Feature
        Regex("feature/chat/ChatViewModel") to DiffCategory.NEW_FEATURE,
        Regex("feature/chat/ChatScreen") to DiffCategory.NEW_FEATURE,
        Regex("feature/voice/") to DiffCategory.NEW_FEATURE,
        Regex("feature/") to DiffCategory.NEW_FEATURE,

        // Helpers
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
            Regex("\\?\\."),
            Regex("\\?:"),
            Regex("try\\s*\\{"),
            Regex("catch\\s*\\("),
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
            Regex("ENABLE_"),
            Regex("BuildConfig"),
            Regex("buildConfigField"),
        ),
    )
}
