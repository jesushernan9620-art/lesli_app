    package com.lesli.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lesli.mobile.core.common.SyncStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String = "",
    val dueDate: String = "Hoy",
    val priority: String = "MEDIA",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = SyncStatus.PENDING.name
)
