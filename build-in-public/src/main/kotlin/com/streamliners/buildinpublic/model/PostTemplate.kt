package com.streamliners.buildinpublic.model

enum class PostTemplate(
    val templateName: String,
    val applicableCategories: List<DiffCategory>,
    val structure: String
) {
    FEATURE_ANNOUNCEMENT(
        templateName = "Feature Announcement",
        applicableCategories = listOf(DiffCategory.NEW_FEATURE),
        structure = """
            |🔨 Building in public: {{APP_NAME}}
            |
            |Just shipped: {{FEATURE_NAME}}
            |
            |The problem:
            |{{PROBLEM_DESCRIPTION}}
            |
            |The solution:
            |{{SOLUTION_DESCRIPTION}}
            |
            |Tech details:
            |{{TECH_BULLETS}}
            |
            |What's next: {{NEXT_FEATURE}}
            |
            |#BuildInPublic #AndroidDev #Kotlin
        """.trimMargin()
    ),

    PROMPT_ENGINEERING(
        templateName = "AI/Prompt Engineering Lesson",
        applicableCategories = listOf(DiffCategory.AI_PROMPT),
        structure = """
            |I spent time tweaking an AI prompt today. Here's what I learned 👇
            |
            |Building {{APP_NAME}}, I use Gemini to {{AI_PURPOSE}}.
            |
            |The change:
            |{{CHANGE_DESCRIPTION}}
            |
            |The result:
            |{{RESULT_DESCRIPTION}}
            |
            |Key takeaway: {{TAKEAWAY}}
            |
            |#PromptEngineering #AI #BuildInPublic #GenerativeAI
        """.trimMargin()
    ),

    ARCHITECTURE_DECISION(
        templateName = "Architecture Decision",
        applicableCategories = listOf(DiffCategory.ARCHITECTURE),
        structure = """
            |Made a key architecture decision for {{APP_NAME}} today.
            |
            |The decision: {{DECISION}}
            |The alternatives: {{ALTERNATIVES}}
            |
            |Why this approach:
            |{{REASONS}}
            |
            |Trade-offs I accepted:
            |{{TRADEOFFS}}
            |
            |Would you have made the same choice?
            |
            |#SoftwareArchitecture #AndroidDev #BuildInPublic
        """.trimMargin()
    ),

    BUG_FIX_STORY(
        templateName = "Bug Fix Story",
        applicableCategories = listOf(DiffCategory.BUG_FIX),
        structure = """
            |Spent time debugging one issue today. The culprit? {{ROOT_CAUSE}}
            |
            |Context: {{CONTEXT}}
            |The symptom: {{SYMPTOM}}
            |
            |My debugging journey:
            |{{DEBUG_STEPS}}
            |
            |The fix was {{FIX_SIZE}} lines of code.
            |Lesson: {{LESSON}}
            |
            |#Debugging #BuildInPublic #AndroidDev
        """.trimMargin()
    ),

    TECHNICAL_DEEP_DIVE(
        templateName = "Technical Deep-Dive",
        applicableCategories = listOf(DiffCategory.DATA_MODEL, DiffCategory.UI_UX),
        structure = """
            |How I built {{FEATURE_NAME}} in {{APP_NAME}} 🧵
            |
            |The goal: {{USER_GOAL}}
            |
            |The tech:
            |{{TECH_STACK}}
            |
            |The interesting part:
            |{{INTERESTING_DETAIL}}
            |
            |```kotlin
            |{{CODE_SNIPPET}}
            |```
            |
            |What made it tricky: {{CHALLENGE}}
            |How I solved it: {{SOLUTION}}
            |
            |#AndroidDev #Kotlin #BuildInPublic
        """.trimMargin()
    ),

    WEEKLY_UPDATE(
        templateName = "Weekly Progress Update",
        applicableCategories = DiffCategory.entries,
        structure = """
            |Week {{WEEK_NUMBER}} building {{APP_NAME}} in public 📊
            |
            |What I shipped:
            |{{SHIPPED_ITEMS}}
            |
            |By the numbers:
            |• {{COMMITS}} commits
            |• {{FILES_CHANGED}} files changed
            |• {{LINES_ADDED}} new lines of code
            |
            |Biggest win: {{WIN}}
            |Biggest challenge: {{CHALLENGE}}
            |
            |Next week's focus: {{NEXT_FOCUS}}
            |
            |#BuildInPublic #IndieHacker #AndroidDev
        """.trimMargin()
    ),

    TECH_STACK_DECISION(
        templateName = "Tech Stack Decision",
        applicableCategories = listOf(DiffCategory.DEPENDENCY),
        structure = """
            |Why I chose {{LIBRARY_A}} over {{LIBRARY_B}} for {{APP_NAME}}
            |
            |The requirement: {{REQUIREMENT}}
            |
            |Options I evaluated:
            |{{OPTIONS}}
            |
            |Winner: {{WINNER}} because {{KEY_REASON}}
            |
            |What's your go-to for this use case?
            |
            |#AndroidDev #TechStack #BuildInPublic
        """.trimMargin()
    );

    companion object {
        fun forCategory(category: DiffCategory): PostTemplate {
            return entries.firstOrNull { category in it.applicableCategories }
                ?: WEEKLY_UPDATE
        }
    }
}
