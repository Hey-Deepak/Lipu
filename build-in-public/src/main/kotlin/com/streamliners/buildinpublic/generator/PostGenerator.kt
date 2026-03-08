package com.streamliners.buildinpublic.generator

import com.streamliners.buildinpublic.model.AnalysisResult
import com.streamliners.buildinpublic.model.AnalysisResult.CommitStats
import com.streamliners.buildinpublic.model.DiffCategory
import com.streamliners.buildinpublic.strategy.LinkedInStrategy

class PostGenerator(
    private val appName: String = LinkedInStrategy.APP_NAME
) {

    fun generateDraft(result: AnalysisResult): String {
        val topFinding = result.topFindings().firstOrNull()
            ?: return generateMinimalPost(result)

        val category = DiffCategory.entries.find { it.name == topFinding.category }
            ?: return generateMinimalPost(result)

        return when (category) {
            DiffCategory.AI_PROMPT -> generatePromptPost(result, topFinding)
            DiffCategory.NEW_FEATURE -> generateFeaturePost(result, topFinding)
            DiffCategory.ARCHITECTURE -> generateArchitecturePost(result, topFinding)
            DiffCategory.BUG_FIX -> generateBugFixPost(result, topFinding)
            DiffCategory.UI_UX -> generateUIPost(result, topFinding)
            DiffCategory.DATA_MODEL -> generateDataModelPost(result, topFinding)
            DiffCategory.DEPENDENCY -> generateDependencyPost(result, topFinding)
            DiffCategory.CONFIG -> generateConfigPost(result, topFinding)
        }
    }

    fun generateWeeklySummary(results: List<AnalysisResult>, weekNumber: Int): String {
        val allFindings = results.flatMap { it.findings }
        val totalStats = CommitStats(
            filesChanged = results.sumOf { it.stats.filesChanged },
            insertions = results.sumOf { it.stats.insertions },
            deletions = results.sumOf { it.stats.deletions },
            newFiles = results.flatMap { it.stats.newFiles },
            deletedFiles = results.flatMap { it.stats.deletedFiles },
            modifiedFiles = results.flatMap { it.stats.modifiedFiles }
        )
        val topCategory = allFindings.groupBy { it.category }
            .maxByOrNull { it.value.size }?.key

        val hashtags = LinkedInStrategy.Hashtags.primary.joinToString(" ")

        return buildString {
            appendLine("Week $weekNumber building $appName in public 📊")
            appendLine()
            appendLine("What I shipped:")
            allFindings.distinctBy { it.title }.take(5).forEach {
                appendLine("✅ ${it.title}")
            }
            appendLine()
            appendLine("By the numbers:")
            appendLine("• ${results.size} commits")
            appendLine("• ${totalStats.filesChanged} files changed")
            appendLine("• +${totalStats.insertions} / -${totalStats.deletions} lines")
            if (totalStats.newFiles.isNotEmpty()) {
                appendLine("• ${totalStats.newFiles.size} new files created")
            }
            appendLine()
            appendLine("Most active area: ${topCategory ?: "various"}")
            appendLine()
            appendLine(hashtags)
        }
    }

    private fun generatePromptPost(result: AnalysisResult, finding: AnalysisResult.Finding): String {
        val hashtags = LinkedInStrategy.Hashtags.aiRelated.joinToString(" ")
        return buildString {
            appendLine("I spent time tweaking an AI prompt today. Here's what I learned 👇")
            appendLine()
            appendLine("Building $appName, I use Gemini for conversational time tracking.")
            appendLine()
            appendLine("What changed:")
            appendLine(finding.detail)
            appendCodeSnippet(finding.codeSnippet)
            appendLine("Commit: ${result.commitMessage}")
            appendLine()
            appendLine("Key takeaway: [Add your lesson here]")
            appendLine()
            appendLine("#BuildInPublic $hashtags")
        }
    }

    private fun generateFeaturePost(result: AnalysisResult, finding: AnalysisResult.Finding): String {
        return buildString {
            appendLine("🔨 Building in public: $appName")
            appendLine()
            appendLine("Just shipped: ${finding.title}")
            appendLine()
            appendLine(finding.detail)
            appendCodeSnippet(finding.codeSnippet)
            appendFilesList(finding.affectedFiles)
            appendLine()
            appendLine("#BuildInPublic #AndroidDev #Kotlin #JetpackCompose")
        }
    }

    private fun generateArchitecturePost(result: AnalysisResult, finding: AnalysisResult.Finding): String {
        return buildString {
            appendLine("Made a key architecture decision for $appName today.")
            appendLine()
            appendLine(finding.title)
            appendLine()
            appendLine(finding.detail)
            appendLine()
            appendLine("Would you have made the same choice?")
            appendLine()
            appendLine("#BuildInPublic #AndroidDev #CleanArchitecture")
        }
    }

    private fun generateBugFixPost(result: AnalysisResult, finding: AnalysisResult.Finding): String {
        return buildString {
            appendLine("Spent time debugging one issue today 🐛")
            appendLine()
            appendLine("Context: ${result.commitMessage}")
            appendLine()
            appendLine(finding.detail)
            appendCodeSnippet(finding.codeSnippet)
            appendLine("#Debugging #BuildInPublic #AndroidDev")
        }
    }

    private fun generateUIPost(result: AnalysisResult, finding: AnalysisResult.Finding): String {
        return buildString {
            appendLine("UI evolution in $appName 🎨")
            appendLine()
            appendLine(finding.title)
            appendLine()
            appendLine(finding.detail)
            appendLine()
            appendLine("[Add before/after screenshots here]")
            appendLine()
            appendLine("#BuildInPublic #JetpackCompose #AndroidDev")
        }
    }

    private fun generateDataModelPost(result: AnalysisResult, finding: AnalysisResult.Finding): String {
        return buildString {
            appendLine("How I modeled data in $appName 🧵")
            appendLine()
            appendLine(finding.title)
            appendLine()
            appendLine(finding.detail)
            appendCodeSnippet(finding.codeSnippet)
            appendLine("#BuildInPublic #AndroidDev #Kotlin #RoomDB")
        }
    }

    private fun generateDependencyPost(result: AnalysisResult, finding: AnalysisResult.Finding): String {
        return buildString {
            appendLine("Tech stack decision for $appName 📦")
            appendLine()
            appendLine(finding.detail)
            appendLine()
            appendLine("What's your go-to for this use case?")
            appendLine()
            appendLine("#BuildInPublic #AndroidDev #TechStack")
        }
    }

    private fun generateConfigPost(result: AnalysisResult, finding: AnalysisResult.Finding): String {
        return buildString {
            appendLine("Configuration update in $appName ⚙️")
            appendLine()
            appendLine(finding.detail)
            appendLine()
            appendLine("#BuildInPublic #AndroidDev")
        }
    }

    private fun generateMinimalPost(result: AnalysisResult): String {
        return buildString {
            appendLine("Building $appName in public 🔨")
            appendLine()
            appendLine("Today's commit: ${result.commitMessage}")
            appendLine()
            appendLine("Stats: ${result.stats.filesChanged} files, +${result.stats.insertions}/-${result.stats.deletions} lines")
            appendLine()
            appendLine("#BuildInPublic #AndroidDev")
        }
    }

    private fun StringBuilder.appendCodeSnippet(snippet: String?) {
        if (snippet != null) {
            appendLine()
            appendLine("```kotlin")
            appendLine(snippet)
            appendLine("```")
            appendLine()
        }
    }

    private fun StringBuilder.appendFilesList(files: List<String>) {
        appendLine("Files: ${files.joinToString { it.substringAfterLast("/") }}")
    }
}
