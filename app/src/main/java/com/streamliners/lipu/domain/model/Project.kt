package com.streamliners.lipu.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
