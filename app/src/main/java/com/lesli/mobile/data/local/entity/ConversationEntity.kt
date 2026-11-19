package com.lesli.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lesli.mobile.core.common.SyncStatus

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val lastMessage: String,
    val dateFormatted: String,
    val messageCount: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val lastActivityAt: Long = System.currentTimeMillis(),
    val syncStatus: String = SyncStatus.PENDING.name
)
