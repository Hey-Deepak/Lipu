package com.streamliners.buildinpublic.generator

import com.streamliners.buildinpublic.model.AnalysisResult
import com.streamliners.buildinpublic.model.DiffCategory

/**
 * Prints a formatted analysis report to the console.
 */
object ReportPrinter {

    fun printReport(result: AnalysisResult) {
        println("=".repeat(60))
        println("  Build-in-Public: Diff Analysis Report")
        println("  Commit: ${result.commitHash.take(7)} ${result.commitMessage}")
        println("  Author: ${result.author}")
        println("  Date: ${result.date}")
        println("=".repeat(60))
        println()

        // Stats
        println("📊 Stats:")
        println("   Files changed: ${result.stats.filesChanged}")
        println("   Insertions: +${result.stats.insertions}")
        println("   Deletions: -${result.stats.deletions}")
        if (result.stats.newFiles.isNotEmpty()) {
            println("   New files: ${result.stats.newFiles.joinToString { it.substringAfterLast("/") }}")
        }
        if (result.stats.deletedFiles.isNotEmpty()) {
            println("   Deleted files: ${result.stats.deletedFiles.joinToString { it.substringAfterLast("/") }}")
        }
        println()

        // Findings
        if (result.findings.isEmpty()) {
            println("No significant findings detected.")
            return
        }

        println("=".repeat(60))
        println("  Detected Categories")
        println("=".repeat(60))
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
                finding.codeSnippet.lines().take(5).forEach { line ->
                    println("     $line")
                }
            }
            println("   → Suggested template: ${finding.suggestedTemplate}")
            println()
        }

        // Suggested post type
        println("=".repeat(60))
        println("  Recommended Post Type")
        println("=".repeat(60))
        val primaryCategory = result.primaryCategory()
        if (primaryCategory != null) {
            println("→ ${primaryCategory.emoji} ${primaryCategory.displayName}: ${primaryCategory.description}")
        }
        println()
    }

    fun printPostDraft(draft: String) {
        println("=".repeat(60))
        println("  LinkedIn Post Draft")
        println("=".repeat(60))
        println()
        println(draft)
        println()
        println("=".repeat(60))
    }
}
