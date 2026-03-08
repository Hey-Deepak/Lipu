package com.streamliners.buildinpublic.strategy

/**
 * LinkedIn Build-in-Public content strategy configuration.
 */
object LinkedInStrategy {

    const val APP_NAME = "Lipu"

    /** Post frequency target per week */
    const val POSTS_PER_WEEK = 4

    /** Best posting times on LinkedIn */
    val bestPostingDays = listOf("Tuesday", "Wednesday", "Thursday")
    val bestPostingTimeRange = "8:00 AM - 10:00 AM"

    /**
     * Content pillars with target percentage of total posts.
     */
    enum class ContentPillar(
        val displayName: String,
        val targetPercentage: Int,
        val topics: List<String>
    ) {
        AI_FEATURES(
            displayName = "AI-Powered Features",
            targetPercentage = 40,
            topics = listOf(
                "Prompt engineering iterations",
                "AI parsing reliability - CSV output from conversation",
                "Natural language to SQL (Insights feature)",
                "Model selection decisions (Gemini 1.5 Flash vs alternatives)",
                "Handling AI hallucinations in structured output",
                "Voice + AI: conversational UX"
            )
        ),
        ANDROID_ENGINEERING(
            displayName = "Android Engineering",
            targetPercentage = 30,
            topics = listOf(
                "Jetpack Compose patterns (MessagesList, AnimatedContent)",
                "Room DB with Flow for reactive chat UI",
                "Koin DI setup for Android",
                "Coroutine patterns (suspendCoroutine for TTS)",
                "Navigation Compose multi-screen architecture",
                "Google Sheets API integration"
            )
        ),
        PRODUCT_BUILDING(
            displayName = "Product / Indie Building",
            targetPercentage = 20,
            topics = listOf(
                "Feature prioritization decisions",
                "Why time tracking needs a conversational approach",
                "User feedback and iterations",
                "The Timify to Lipu rebrand story",
                "Shipping incomplete features (PieChart, SheetSync stubs)"
            )
        ),
        WEEKLY_UPDATES(
            displayName = "Weekly Updates",
            targetPercentage = 10,
            topics = listOf(
                "Weekly commit summaries",
                "Metrics (lines changed, features shipped)",
                "Roadmap updates"
            )
        )
    }

    /**
     * Hashtag sets to rotate through.
     */
    object Hashtags {

        val primary = listOf("#BuildInPublic", "#AndroidDev", "#IndieHacker")

        val aiRelated = listOf("#PromptEngineering", "#AI", "#GenerativeAI", "#Gemini")
        val androidRelated = listOf("#Kotlin", "#JetpackCompose", "#MobileDev")
        val productRelated = listOf("#ProductDevelopment", "#StartupJourney")
        val architectureRelated = listOf("#CleanArchitecture", "#SoftwareEngineering")

        fun forPillar(pillar: ContentPillar): List<String> {
            val specific = when (pillar) {
                ContentPillar.AI_FEATURES -> aiRelated
                ContentPillar.ANDROID_ENGINEERING -> androidRelated
                ContentPillar.PRODUCT_BUILDING -> productRelated
                ContentPillar.WEEKLY_UPDATES -> primary
            }
            return primary.take(2) + specific.take(2)
        }
    }

    /**
     * Engagement tips to apply to every post.
     */
    val engagementTips = listOf(
        "Ask a question at the end of the post",
        "Share code snippets - technical posts get saved and shared",
        "Show vulnerability - share failures, bugs, wrong decisions",
        "Be specific - '1.0 to 0.7 temperature' beats 'tuned the AI'",
        "Add visuals - screenshots, architecture diagrams, before/after"
    )
}
