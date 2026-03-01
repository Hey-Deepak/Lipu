package com.streamliners.lipu.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("LearningEntries")
data class LearningEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val projectName: String,
    val summary: String,
    val contentType: String = "tweet",
    val generatedContent: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
