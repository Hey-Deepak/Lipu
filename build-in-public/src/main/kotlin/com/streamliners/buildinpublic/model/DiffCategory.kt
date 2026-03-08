package com.streamliners.buildinpublic.model

enum class DiffCategory(
    val displayName: String,
    val emoji: String,
    val priority: Priority,
    val description: String
) {
    AI_PROMPT(
        displayName = "AI/Prompt Engineering",
        emoji = "🤖",
        priority = Priority.VERY_HIGH,
        description = "Changes to AI system prompts, model config, or generation parameters"
    ),
    NEW_FEATURE(
        displayName = "New Feature",
        emoji = "🚀",
        priority = Priority.HIGH,
        description = "New screens, ViewModel functions, DAOs, or user-facing capabilities"
    ),
    ARCHITECTURE(
        displayName = "Architecture",
        emoji = "🏗️",
        priority = Priority.HIGH,
        description = "DI changes, package restructuring, new patterns introduced"
    ),
    BUG_FIX(
        displayName = "Bug Fix",
        emoji = "🐛",
        priority = Priority.MEDIUM_HIGH,
        description = "Targeted fixes, null safety, error handling, edge cases"
    ),
    UI_UX(
        displayName = "UI/UX",
        emoji = "🎨",
        priority = Priority.MEDIUM,
        description = "Compose components, theme, animations, layout changes"
    ),
    DATA_MODEL(
        displayName = "Data Model",
        emoji = "💾",
        priority = Priority.MEDIUM,
        description = "Entity changes, DB migrations, DAO queries, schema evolution"
    ),
    DEPENDENCY(
        displayName = "Dependency",
        emoji = "📦",
        priority = Priority.LOW_MEDIUM,
        description = "Library additions, version bumps, plugin changes"
    ),
    CONFIG(
        displayName = "Configuration",
        emoji = "⚙️",
        priority = Priority.LOW,
        description = "Feature flags, build config, manifest, SDK versions"
    );

    enum class Priority(val weight: Int) {
        VERY_HIGH(5),
        HIGH(4),
        MEDIUM_HIGH(3),
        MEDIUM(2),
        LOW_MEDIUM(1),
        LOW(0)
    }
}
