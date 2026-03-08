package com.streamliners.buildinpublic

import com.streamliners.buildinpublic.analyzer.GitDiffAnalyzer
import com.streamliners.buildinpublic.generator.PostGenerator
import com.streamliners.buildinpublic.generator.ReportPrinter
import java.io.File

fun main(args: Array<String>) {
    val repoPath = findRepoRoot()
    val analyzer = GitDiffAnalyzer(repoPath)
    val postGenerator = PostGenerator()

    when {
        args.isEmpty() -> {
            println("Analyzing HEAD commit...")
            println()
            val result = analyzer.analyzeCommit("HEAD")
            ReportPrinter.printReport(result)
            val draft = postGenerator.generateDraft(result)
            ReportPrinter.printPostDraft(draft)
        }

        args[0] == "weekly" -> {
            val fromCommit = args.getOrElse(1) { "HEAD~7" }
            println("Generating weekly summary from $fromCommit to HEAD...")
            println()
            val results = analyzer.analyzeRange(fromCommit)
            val summary = postGenerator.generateWeeklySummary(results, weekNumber = 1)
            ReportPrinter.printPostDraft(summary)
        }

        else -> {
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

private fun findRepoRoot(): String {
    var dir = File(System.getProperty("user.dir"))
    while (dir.parentFile != null) {
        if (File(dir, ".git").exists()) return dir.absolutePath
        dir = dir.parentFile
    }
    return System.getProperty("user.dir")
}
