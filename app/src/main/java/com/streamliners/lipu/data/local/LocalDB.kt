package com.streamliners.lipu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.streamliners.lipu.data.local.dao.ChatHistoryDao
import com.streamliners.lipu.data.local.dao.LearningEntryDao
import com.streamliners.lipu.data.local.dao.ProjectDao
import com.streamliners.lipu.domain.model.ChatHistoryItem
import com.streamliners.lipu.domain.model.LearningEntry
import com.streamliners.lipu.domain.model.Project

@Database(
    entities = [ChatHistoryItem::class, LearningEntry::class, Project::class],
    version = 3,
    exportSchema = false
)
abstract class LocalDB : RoomDatabase() {

    companion object {
        fun create(context: Context): LocalDB {
            return Room.databaseBuilder(
                context = context,
                klass = LocalDB::class.java,
                name = "lipuDB"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun chatHistoryDao(): ChatHistoryDao

    abstract fun learningEntryDao(): LearningEntryDao

    abstract fun projectDao(): ProjectDao
}