package com.streamliners.buildinpublic.generator

import com.streamliners.buildinpublic.model.AnalysisResult
import com.streamliners.buildinpublic.model.DiffCategory

object ReportPrinter {

    private const val SEPARATOR = "============================================================"

    fun printReport(result: AnalysisResult) {
        println(SEPARATOR)
        println("  Build-in-Public: Diff Analysis Report")
        println("  Commit: ${result.commitHash.take(7)} ${result.commitMessage}")
        println("  Author: ${result.author}")
        println("  Date:   ${result.date}")
        println(SEPARATOR)
        println()

        println("📊 Stats:")
        println("   Files changed: ${result.stats.filesChanged}")
        println("   Insertions:    +${result.stats.insertions}")
        println("   Deletions:     -${result.stats.deletions}")
        if (result.stats.newFiles.isNotEmpty()) {
            println("   New files:     ${result.stats.newFiles.joinToString { it.substringAfterLast("/") }}")
        }
        if (result.stats.deletedFiles.isNotEmpty()) {
            println("   Deleted:       ${result.stats.deletedFiles.joinToString { it.substringAfterLast("/") }}")
        }
        println()

        if (result.findings.isEmpty()) {
            println("No significant findings detected.")
            return
        }

        println(SEPARATOR)
        println("  Detected Categories")
        println(SEPARATOR)
        println()

        for (finding in result.topFindings()) {
            val category = DiffCategory.entries.find { it.name == finding.category }
            val emoji = category?.emoji ?: "📌"
            val priority = category?.priority?.name ?: "UNKNOWN"

            println("$emoji ${finding.title} [$priority PRIORITY]")
            println("   ${finding.detail}")
            println("   Files: ${finding.affectedFiles.joinToString { it.substringAfterLast("/") }}")
            if (finding.codeSnippet != null) {
                println("   Code preview:")
                finding.codeSnippet.lines().take(5).forEach { println("     $it") }
            }
            println("   → Suggested template: ${finding.suggestedTemplate}")
            println()
        }

        println(SEPARATOR)
        println("  Recommended Post Type")
        println(SEPARATOR)
        result.primaryCategory()?.let { category ->
            println("→ ${category.emoji} ${category.displayName}: ${category.description}")
        }
        println()
    }

    fun printPostDraft(draft: String) {
        println(SEPARATOR)
        println("  LinkedIn Post Draft")
        println(SEPARATOR)
        println()
        println(draft)
        println(SEPARATOR)
    }
}
