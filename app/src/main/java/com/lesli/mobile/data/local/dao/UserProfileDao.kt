package com.lesli.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lesli.mobile.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    // 1. SELECT
    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = :id")
    suspend fun getUserProfileById(id: String): UserProfileEntity?

    // 2. INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // 3. UPDATE
    @Update
    suspend fun updateProfile(profile: UserProfileEntity)

    // 4. DELETE
    @Delete
    suspend fun deleteProfile(profile: UserProfileEntity)

    @Query("DELETE FROM user_profile")
    suspend fun deleteProfile()
}
