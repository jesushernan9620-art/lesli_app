package com.lesli.mobile.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lesli.mobile.data.local.dao.ChatMessageDao
import com.lesli.mobile.data.local.dao.ConversationDao
import com.lesli.mobile.data.local.dao.TaskDao
import com.lesli.mobile.data.local.dao.UserProfileDao
import com.lesli.mobile.data.local.entity.ChatMessageEntity
import com.lesli.mobile.data.local.entity.ConversationEntity
import com.lesli.mobile.data.local.entity.TaskEntity
import com.lesli.mobile.data.local.entity.UserProfileEntity

@Database(
    entities = [
        TaskEntity::class,
        ConversationEntity::class,
        ChatMessageEntity::class,
        UserProfileEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class LesliDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun conversationDao(): ConversationDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: LesliDatabase? = null

        fun getInstance(context: Context): LesliDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LesliDatabase::class.java,
                    "lesli_mobile_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
