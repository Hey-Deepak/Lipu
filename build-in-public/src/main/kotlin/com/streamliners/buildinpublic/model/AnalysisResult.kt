package com.streamliners.buildinpublic.model

import kotlinx.serialization.Serializable

/**
 * Result of analyzing a git commit/diff for post-worthy content.
 */
@Serializable
data class AnalysisResult(
    val commitHash: String,
    val commitMessage: String,
    val author: String,
    val date: String,
    val findings: List<Finding>,
    val stats: CommitStats
) {
    /**
     * A single post-worthy finding extracted from the diff.
     */
    @Serializable
    data class Finding(
        val category: String, // DiffCategory name
        val title: String,
        val detail: String,
        val affectedFiles: List<String>,
        val codeSnippet: String? = null,
        val suggestedTemplate: String
    )

    @Serializable
    data class CommitStats(
        val filesChanged: Int,
        val insertions: Int,
        val deletions: Int,
        val newFiles: List<String>,
        val deletedFiles: List<String>,
        val modifiedFiles: List<String>
    )

    /**
     * Returns findings sorted by priority (highest first).
     */
    fun topFindings(): List<Finding> {
        val priorityOrder = DiffCategory.entries.map { it.name }
        return findings.sortedBy { finding ->
            priorityOrder.indexOf(finding.category)
        }
    }

    fun primaryCategory(): DiffCategory? {
        return topFindings().firstOrNull()?.let { finding ->
            DiffCategory.entries.find { it.name == finding.category }
        }
    }
}
