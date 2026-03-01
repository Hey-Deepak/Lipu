package com.streamliners.lipu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streamliners.lipu.domain.model.LearningEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningEntryDao {

    @Query("SELECT * FROM LearningEntries WHERE date = :date")
    suspend fun getByDate(date: String): List<LearningEntry>

    @Query("SELECT * FROM LearningEntries ORDER BY createdAt DESC")
    fun getAll(): Flow<List<LearningEntry>>

    @Query("SELECT * FROM LearningEntries ORDER BY createdAt DESC LIMIT :limit")
    fun getRecent(limit: Int = 10): Flow<List<LearningEntry>>

    @Query("SELECT DISTINCT projectName FROM LearningEntries")
    suspend fun getProjectNames(): List<String>

    @Insert
    suspend fun add(entry: LearningEntry)

    @Query("DELETE FROM LearningEntries WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT COUNT(*) FROM LearningEntries")
    suspend fun getTotalCount(): Int

}