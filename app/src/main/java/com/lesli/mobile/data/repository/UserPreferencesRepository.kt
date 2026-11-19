package com.lesli.mobile.data.repository

import com.lesli.mobile.core.datastore.UserPreferencesDataStore
import com.lesli.mobile.data.local.dao.UserProfileDao
import com.lesli.mobile.data.local.entity.UserProfileEntity
import com.lesli.mobile.data.mapper.toDomainModel
import com.lesli.mobile.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface UserPreferencesRepository {
    val userProfile: Flow<UserProfile>
    suspend fun updateProfile(name: String, email: String)
    suspend fun toggleNotifications(enabled: Boolean)
    suspend fun toggleDarkMode(enabled: Boolean)
    suspend fun toggleSound(enabled: Boolean)
}

class UserPreferencesRepositoryImpl(
    private val userProfileDao: UserProfileDao,
    private val preferencesDataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    init {
        // Pre-populate user profile in Room if database is empty
        CoroutineScope(Dispatchers.IO).launch {
            if (userProfileDao.getUserProfile().first() == null) {
                userProfileDao.insertOrUpdateProfile(UserProfileEntity())
            }
        }
    }

    override val userProfile: Flow<UserProfile> = combine(
        userProfileDao.getUserProfile(),
        preferencesDataStore.userPreferencesFlow
    ) { profileEntity, preferences ->
        val entity = profileEntity ?: UserProfileEntity()
        entity.toDomainModel(preferences)
    }

    override suspend fun updateProfile(name: String, email: String) {
        val currentEntity = userProfileDao.getUserProfile().first() ?: UserProfileEntity()
        val updated = currentEntity.copy(
            name = name.ifBlank { "Bernardo" },
            email = email.ifBlank { "bernardo@ejemplo.com" }
        )
        userProfileDao.insertOrUpdateProfile(updated)
    }

    override suspend fun toggleNotifications(enabled: Boolean) {
        preferencesDataStore.updateNotifications(enabled)
    }

    override suspend fun toggleDarkMode(enabled: Boolean) {
        preferencesDataStore.updateDarkMode(enabled)
    }

    override suspend fun toggleSound(enabled: Boolean) {
        preferencesDataStore.updateSound(enabled)
    }
}
