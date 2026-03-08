package com.streamliners.buildinpublic

import com.streamliners.buildinpublic.analyzer.GitDiffAnalyzer
import com.streamliners.buildinpublic.generator.PostGenerator
import com.streamliners.buildinpublic.generator.ReportPrinter

/**
 * Build-in-Public CLI Tool
 *
 * Analyzes Lipu git commits to auto-extract interesting findings
 * and generate LinkedIn post drafts.
 *
 * Usage:
 *   ./gradlew run                          # Analyze HEAD commit
 *   ./gradlew run --args="<commit-hash>"   # Analyze specific commit
 *   ./gradlew run --args="weekly <from>"   # Weekly summary from commit
 */
fun main(args: Array<String>) {
    val repoPath = findRepoRoot()
    val analyzer = GitDiffAnalyzer(repoPath)
    val postGenerator = PostGenerator(appName = "Lipu")

    when {
        args.isEmpty() -> {
            // Analyze latest commit
            println("Analyzing HEAD commit...")
            println()

            val result = analyzer.analyzeCommit("HEAD")
            ReportPrinter.printReport(result)

            val draft = postGenerator.generateDraft(result)
            ReportPrinter.printPostDraft(draft)
        }

        args[0] == "weekly" -> {
            // Weekly summary
            val fromCommit = args.getOrElse(1) { "HEAD~7" }
            println("Generating weekly summary from $fromCommit to HEAD...")
            println()

            val results = analyzer.analyzeRange(fromCommit)
            val summary = postGenerator.generateWeeklySummary(results, weekNumber = 1)
            ReportPrinter.printPostDraft(summary)
        }

        else -> {
            // Analyze specific commit
            val commitHash = args[0]
            println("Analyzing commit $commitHash...")
            println()

            val result = analyzer.analyzeCommit(commitHash)
            ReportPrinter.printReport(result)

            val draft = postGenerator.generateDraft(result)
            ReportPrinter.printPostDraft(draft)
        }
    }
}

/**
 * Walk up from current directory to find the git repository root.
 */
private fun findRepoRoot(): String {
    var dir = java.io.File(System.getProperty("user.dir"))
    while (dir.parentFile != null) {
        if (java.io.File(dir, ".git").exists()) return dir.absolutePath
        dir = dir.parentFile
    }
    // Fallback: assume current dir
    return System.getProperty("user.dir")
}
