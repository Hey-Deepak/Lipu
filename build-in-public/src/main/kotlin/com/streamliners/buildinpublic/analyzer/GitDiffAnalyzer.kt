package com.streamliners.buildinpublic.analyzer

import com.streamliners.buildinpublic.model.AnalysisResult
import com.streamliners.buildinpublic.model.AnalysisResult.CommitStats
import com.streamliners.buildinpublic.model.AnalysisResult.Finding
import com.streamliners.buildinpublic.model.DiffCategory
import com.streamliners.buildinpublic.model.FileClassification
import com.streamliners.buildinpublic.model.PostTemplate
import com.streamliners.buildinpublic.util.GitCommand

/**
 * Analyzes git diffs to extract post-worthy findings for LinkedIn.
 */
class GitDiffAnalyzer(
    private val repoPath: String
) {
    private val git = GitCommand(repoPath)

    /**
     * Analyze a single commit and return categorized findings.
     */
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
        val categorizedFiles = changedFiles.groupBy { filePath ->
            FileClassification.classify(filePath)
        }.filterKeys { it != null }.mapKeys { it.key!! }

        // Analyze each category group
        for ((category, files) in categorizedFiles) {
            val finding = analyzeCategoryFiles(commitHash, category, files)
            if (finding != null) {
                findings.add(finding)
            }
        }

        // Content-based detection (scan diff content for signals)
        val fullDiff = git.getFullDiff(commitHash)
        val contentFindings = analyzeContentSignals(fullDiff, changedFiles, findings)
        findings.addAll(contentFindings)

        // Detect bug fixes from small, targeted changes
        if (findings.isEmpty() && stats.filesChanged <= 3 && stats.insertions + stats.deletions < 20) {
            findings.add(
                Finding(
                    category = DiffCategory.BUG_FIX.name,
                    title = "Small targeted fix",
                    detail = "Small change across ${stats.filesChanged} file(s) - likely a bug fix or tweak",
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

    /**
     * Analyze a range of commits (e.g., for weekly summary).
     */
    fun analyzeRange(fromCommit: String, toCommit: String = "HEAD"): List<AnalysisResult> {
        val commits = git.getCommitRange(fromCommit, toCommit)
        return commits.map { analyzeCommit(it) }
    }

    private fun analyzeCategoryFiles(
        commitHash: String,
        category: DiffCategory,
        files: List<String>
    ): Finding? {
        val diff = files.mapNotNull { git.getFileDiff(commitHash, it) }.joinToString("\n")
        if (diff.isBlank()) return null

        val title = generateTitle(category, files, diff)
        val detail = generateDetail(category, files, diff)
        val snippet = extractCodeSnippet(diff)

        return Finding(
            category = category.name,
            title = title,
            detail = detail,
            affectedFiles = files,
            codeSnippet = snippet,
            suggestedTemplate = PostTemplate.forCategory(category).templateName
        )
    }

    private fun analyzeContentSignals(
        fullDiff: String,
        allFiles: List<String>,
        existingFindings: List<Finding>
    ): List<Finding> {
        val existingCategories = existingFindings.map { it.category }.toSet()
        val findings = mutableListOf<Finding>()

        for ((category, signals) in FileClassification.contentSignals) {
            if (category.name in existingCategories) continue

            val matchedSignals = signals.filter { it.containsMatchIn(fullDiff) }
            if (matchedSignals.size >= 2) {
                findings.add(
                    Finding(
                        category = category.name,
                        title = "${category.displayName} detected via content analysis",
                        detail = "Found ${matchedSignals.size} signals: ${matchedSignals.joinToString { it.pattern }}",
                        affectedFiles = allFiles,
                        suggestedTemplate = PostTemplate.forCategory(category).templateName
                    )
                )
            }
        }

        return findings
    }

    private fun generateTitle(category: DiffCategory, files: List<String>, diff: String): String {
        return when (category) {
            DiffCategory.AI_PROMPT -> {
                when {
                    diff.contains("modelName") -> "AI model changed"
                    diff.contains("SYSTEM_INSTRUCTION") -> "System prompt updated"
                    diff.contains("temperature") || diff.contains("topK") -> "Generation config tuned"
                    else -> "AI configuration updated"
                }
            }
            DiffCategory.NEW_FEATURE -> {
                val featureName = files.firstOrNull()
                    ?.substringAfterLast("/")
                    ?.removeSuffix(".kt")
                    ?: "New feature"
                "New feature: $featureName"
            }
            DiffCategory.UI_UX -> {
                val componentName = files.firstOrNull()
                    ?.substringAfterLast("/")
                    ?.removeSuffix(".kt")
                    ?: "UI component"
                "UI update: $componentName"
            }
            DiffCategory.DATA_MODEL -> {
                val entityName = Regex("class\\s+(\\w+)").find(diff)?.groupValues?.get(1)
                    ?: "data model"
                "Data model change: $entityName"
            }
            DiffCategory.ARCHITECTURE -> "Architecture change in ${files.size} file(s)"
            DiffCategory.DEPENDENCY -> "Dependency update"
            DiffCategory.CONFIG -> "Configuration change"
            DiffCategory.BUG_FIX -> "Bug fix"
        }
    }

    private fun generateDetail(category: DiffCategory, files: List<String>, diff: String): String {
        val addedLines = diff.lines().count { it.startsWith("+") && !it.startsWith("+++") }
        val removedLines = diff.lines().count { it.startsWith("-") && !it.startsWith("---") }

        return buildString {
            append("${files.size} file(s) affected. ")
            append("+$addedLines/-$removedLines lines. ")
            append("Files: ${files.joinToString { it.substringAfterLast("/") }}")
        }
    }

    private fun extractCodeSnippet(diff: String, maxLines: Int = 10): String? {
        val addedLines = diff.lines()
            .filter { it.startsWith("+") && !it.startsWith("+++") }
            .map { it.removePrefix("+") }
            .filter { it.isNotBlank() }

        if (addedLines.isEmpty()) return null

        return addedLines
            .take(maxLines)
            .joinToString("\n")
    }

    private fun parseStats(
        diffSummary: String,
        newFiles: List<String>,
        deletedFiles: List<String>,
        modifiedFiles: List<String>
    ): CommitStats {
        val insertions = Regex("(\\d+) insertion").find(diffSummary)
            ?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val deletions = Regex("(\\d+) deletion").find(diffSummary)
            ?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val filesChanged = Regex("(\\d+) file").find(diffSummary)
            ?.groupValues?.get(1)?.toIntOrNull() ?: 0

        return CommitStats(
            filesChanged = filesChanged,
            insertions = insertions,
            deletions = deletions,
            newFiles = newFiles,
            deletedFiles = deletedFiles,
            modifiedFiles = modifiedFiles
        )
    }
}
