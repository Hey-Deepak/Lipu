package com.streamliners.buildinpublic.strategy

import com.streamliners.buildinpublic.strategy.LinkedInStrategy.ContentPillar

object PostIdeas {

    data class PostIdea(
        val title: String,
        val angle: String,
        val pillar: ContentPillar,
        val keyCodeReferences: List<String>,
        val hookLine: String
    )

    val ideas = listOf(
        PostIdea(
            title = "AI time tracker - why chat beats forms",
            angle = "Product vision",
            pillar = ContentPillar.PRODUCT_BUILDING,
            keyCodeReferences = listOf("GeminiModel.kt - NORMAL_CHAT_SYSTEM_INSTRUCTION"),
            hookLine = "I'm building an AI time tracker. Here's why chat beats forms."
        ),
        PostIdea(
            title = "Teaching Gemini to output structured CSV from casual conversation",
            angle = "Prompt engineering",
            pillar = ContentPillar.AI_FEATURES,
            keyCodeReferences = listOf(
                "GeminiModel.kt - CSV format specification",
                "ChatViewModel.kt - saveTaskInfoToLocal() parsing logic"
            ),
            hookLine = "How I taught Gemini to output structured CSV from casual conversation 👇"
        ),
        PostIdea(
            title = "Voice-first Android: Making TTS work with Kotlin Coroutines",
            angle = "Technical deep-dive",
            pillar = ContentPillar.ANDROID_ENGINEERING,
            keyCodeReferences = listOf("TTSHelper.kt - suspendCoroutine wrapper pattern"),
            hookLine = "Android TTS API is callback-based. Here's how I made it work with coroutines."
        ),
        PostIdea(
            title = "Natural Language to SQL with Gemini",
            angle = "AI/Engineering",
            pillar = ContentPillar.AI_FEATURES,
            keyCodeReferences = listOf(
                "GeminiModel.kt - INSIGHTS_CHAT_SYSTEM_INSTRUCTION",
                "InsightsChat.kt - SQL execution on Room DB"
            ),
            hookLine = "I built an insights engine that turns questions into SQL queries using Gemini."
        ),
        PostIdea(
            title = "From Timify to Lipu: renaming mid-development",
            angle = "Product/Branding",
            pillar = ContentPillar.PRODUCT_BUILDING,
            keyCodeReferences = listOf("Mixed package names: com.streamliners.timify vs com.streamliners.lipu"),
            hookLine = "I renamed my app mid-development. The codebase still shows the scars."
        ),
        PostIdea(
            title = "Room + Flow for a reactive chat UI",
            angle = "Android engineering",
            pillar = ContentPillar.ANDROID_ENGINEERING,
            keyCodeReferences = listOf(
                "ChatHistoryDao.kt - getList() returns Flow",
                "ChatViewModel.kt - loadChat() with collectLatest"
            ),
            hookLine = "I use Room + Flow for a reactive chat UI. Here's the pattern."
        )
    )
}
