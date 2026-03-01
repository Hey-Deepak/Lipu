package com.streamliners.lipu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streamliners.lipu.domain.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM Projects ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Project>>

    @Insert
    suspend fun add(project: Project)

    @Query("DELETE FROM Projects WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT COUNT(*) FROM Projects")
    suspend fun getTotalCount(): Int

}