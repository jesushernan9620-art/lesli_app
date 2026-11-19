package com.lesli.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lesli.mobile.core.common.SyncStatus

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: String = "primary_user",
    val name: String = "Bernardo",
    val email: String = "bernardo@ejemplo.com",
    val role: String = "Usuario Lesli Mobile",
    val syncStatus: String = SyncStatus.PENDING.name
)
