package com.streamliners.buildinpublic.util

import java.io.File

/**
 * Wrapper around git CLI commands.
 * Executes git operations on the given repository path.
 */
class GitCommand(private val repoPath: String) {

    data class CommitInfo(
        val hash: String,
        val message: String,
        val author: String,
        val date: String
    )

    fun getCommitInfo(commitHash: String = "HEAD"): CommitInfo {
        val format = "%H%n%s%n%an%n%ai"
        val output = exec("git log -1 --format=$format $commitHash")
        val lines = output.lines()
        return CommitInfo(
            hash = lines.getOrElse(0) { commitHash },
            message = lines.getOrElse(1) { "" },
            author = lines.getOrElse(2) { "" },
            date = lines.getOrElse(3) { "" }
        )
    }

    fun getChangedFiles(commitHash: String = "HEAD"): List<String> {
        return exec("git diff --name-only ${commitHash}~1 $commitHash")
            .lines()
            .filter { it.isNotBlank() }
    }

    fun getNewFiles(commitHash: String = "HEAD"): List<String> {
        return exec("git diff --diff-filter=A --name-only ${commitHash}~1 $commitHash")
            .lines()
            .filter { it.isNotBlank() }
    }

    fun getDeletedFiles(commitHash: String = "HEAD"): List<String> {
        return exec("git diff --diff-filter=D --name-only ${commitHash}~1 $commitHash")
            .lines()
            .filter { it.isNotBlank() }
    }

    fun getDiffSummary(commitHash: String = "HEAD"): String {
        return exec("git diff --stat ${commitHash}~1 $commitHash")
            .lines()
            .lastOrNull { it.isNotBlank() } ?: ""
    }

    fun getFullDiff(commitHash: String = "HEAD"): String {
        return exec("git diff ${commitHash}~1 $commitHash")
    }

    fun getFileDiff(commitHash: String, filePath: String): String? {
        val output = exec("git diff ${commitHash}~1 $commitHash -- $filePath")
        return output.ifBlank { null }
    }

    fun getCommitRange(fromCommit: String, toCommit: String = "HEAD"): List<String> {
        return exec("git log --format=%H $fromCommit..$toCommit")
            .lines()
            .filter { it.isNotBlank() }
    }

    fun getCommitCount(since: String): Int {
        return exec("git rev-list --count --since=$since HEAD")
            .trim()
            .toIntOrNull() ?: 0
    }

    fun getWeeklyStats(): String {
        return exec("git diff --stat @{1.week.ago} HEAD")
    }

    private fun exec(command: String): String {
        return try {
            val process = ProcessBuilder(command.split(" "))
                .directory(File(repoPath))
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            output
        } catch (e: Exception) {
            System.err.println("Git command failed: $command")
            System.err.println("Error: ${e.message}")
            ""
        }
    }
}
