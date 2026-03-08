package com.streamliners.buildinpublic.analyzer

import com.streamliners.buildinpublic.model.AnalysisResult
import com.streamliners.buildinpublic.model.AnalysisResult.CommitStats
import com.streamliners.buildinpublic.model.AnalysisResult.Finding
import com.streamliners.buildinpublic.model.DiffCategory
import com.streamliners.buildinpublic.model.FileClassification
import com.streamliners.buildinpublic.model.PostTemplate
import com.streamliners.buildinpublic.util.GitCommand

class GitDiffAnalyzer(
    private val repoPath: String
) {
    private val git = GitCommand(repoPath)

    fun analyzeCommit(commitHash: String = "HEAD"): AnalysisResult {
        val commitInfo = git.getCommitInfo(commitHash)
        val diffSummary = git.getDiffSummary(commitHash)
        val changedFiles = git.getChangedFiles(commitHash)
        val newFiles = git.getNewFiles(commitHash)
        val deletedFiles = git.getDeletedFiles(commitHash)
        val modifiedFiles = changedFiles - newFiles.toSet() - deletedFiles.toSet()

        val stats = parseStats(diffSummary, newFiles, deletedFiles, modifiedFiles.toList())
        val findings = mutableListOf<Finding>()

        // Group files by category
        val categorizedFiles = changedFiles
            .groupBy { FileClassification.classify(it) }
            .filterKeys { it != null }
            .mapKeys { it.key!! }

        for ((category, files) in categorizedFiles) {
            analyzeCategoryFiles(commitHash, category, files)?.let { findings.add(it) }
        }

        // Content-based detection
        val fullDiff = git.getFullDiff(commitHash)
        findings.addAll(analyzeContentSignals(fullDiff, changedFiles, findings))

        // Small change = likely bug fix
        if (findings.isEmpty() && stats.filesChanged <= 3 && stats.insertions + stats.deletions < 20) {
            findings.add(
                Finding(
                    category = DiffCategory.BUG_FIX.name,
                    title = "Small targeted fix",
                    detail = "Small change across ${stats.filesChanged} file(s)",
                    affectedFiles = changedFiles,
                    suggestedTemplate = PostTemplate.BUG_FIX_STORY.templateName
                )
            )
        }

        return AnalysisResult(
            commitHash = commitInfo.hash,
            commitMessage = commitInfo.message,
            author = commitInfo.author,
            date = commitInfo.date,
            findings = findings.distinctBy { it.category },
            stats = stats
        )
    }

    fun analyzeRange(fromCommit: String, toCommit: String = "HEAD"): List<AnalysisResult> {
        return git.getCommitRange(fromCommit, toCommit).map { analyzeCommit(it) }
    }

    private fun analyzeCategoryFiles(
        commitHash: String,
        category: DiffCategory,
        files: List<String>
    ): Finding? {
        val diff = files.mapNotNull { git.getFileDiff(commitHash, it) }.joinToString("\n")
        if (diff.isBlank()) return null

        return Finding(
            category = category.name,
            title = generateTitle(category, files, diff),
            detail = generateDetail(files, diff),
            affectedFiles = files,
            codeSnippet = extractCodeSnippet(diff),
            suggestedTemplate = PostTemplate.forCategory(category).templateName
        )
    }

    private fun analyzeContentSignals(
        fullDiff: String,
        allFiles: List<String>,
        existingFindings: List<Finding>
    ): List<Finding> {
        val existingCategories = existingFindings.map { it.category }.toSet()

        return FileClassification.contentSignals
            .filter { (category, _) -> category.name !in existingCategories }
            .mapNotNull { (category, signals) ->
                val matched = signals.filter { it.containsMatchIn(fullDiff) }
                if (matched.size >= 2) {
                    Finding(
                        category = category.name,
                        title = "${category.displayName} detected via content analysis",
                        detail = "Found ${matched.size} signals: ${matched.joinToString { it.pattern }}",
                        affectedFiles = allFiles,
                        suggestedTemplate = PostTemplate.forCategory(category).templateName
                    )
                } else null
            }
    }

    private fun generateTitle(category: DiffCategory, files: List<String>, diff: String): String {
        return when (category) {
            DiffCategory.AI_PROMPT -> when {
                "modelName" in diff -> "AI model changed"
                "SYSTEM_INSTRUCTION" in diff -> "System prompt updated"
                "temperature" in diff || "topK" in diff -> "Generation config tuned"
                else -> "AI configuration updated"
            }
            DiffCategory.NEW_FEATURE -> {
                val name = files.firstOrNull()?.substringAfterLast("/")?.removeSuffix(".kt") ?: "New feature"
                "New feature: $name"
            }
            DiffCategory.UI_UX -> {
                val name = files.firstOrNull()?.substringAfterLast("/")?.removeSuffix(".kt") ?: "UI component"
                "UI update: $name"
            }
            DiffCategory.DATA_MODEL -> {
                val entity = Regex("class\\s+(\\w+)").find(diff)?.groupValues?.get(1) ?: "data model"
                "Data model change: $entity"
            }
            DiffCategory.ARCHITECTURE -> "Architecture change in ${files.size} file(s)"
            DiffCategory.DEPENDENCY -> "Dependency update"
            DiffCategory.CONFIG -> "Configuration change"
            DiffCategory.BUG_FIX -> "Bug fix"
        }
    }

    private fun generateDetail(files: List<String>, diff: String): String {
        val added = diff.lines().count { it.startsWith("+") && !it.startsWith("+++") }
        val removed = diff.lines().count { it.startsWith("-") && !it.startsWith("---") }
        return "${files.size} file(s) affected. +$added/-$removed lines. Files: ${files.joinToString { it.substringAfterLast("/") }}"
    }

    private fun extractCodeSnippet(diff: String, maxLines: Int = 10): String? {
        val added = diff.lines()
            .filter { it.startsWith("+") && !it.startsWith("+++") }
            .map { it.removePrefix("+") }
            .filter { it.isNotBlank() }

        return added.takeIf { it.isNotEmpty() }?.take(maxLines)?.joinToString("\n")
    }

    private fun parseStats(
        diffSummary: String,
        newFiles: List<String>,
        deletedFiles: List<String>,
        modifiedFiles: List<String>
    ): CommitStats {
        return CommitStats(
            filesChanged = Regex("(\\d+) file").find(diffSummary)?.groupValues?.get(1)?.toIntOrNull() ?: 0,
            insertions = Regex("(\\d+) insertion").find(diffSummary)?.groupValues?.get(1)?.toIntOrNull() ?: 0,
            deletions = Regex("(\\d+) deletion").find(diffSummary)?.groupValues?.get(1)?.toIntOrNull() ?: 0,
            newFiles = newFiles,
            deletedFiles = deletedFiles,
            modifiedFiles = modifiedFiles
        )
    }
}
