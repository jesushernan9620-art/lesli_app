package com.lesli.mobile.data.mapper

import com.lesli.mobile.core.datastore.UserPreferences
import com.lesli.mobile.data.local.entity.UserProfileEntity
import com.lesli.mobile.data.model.UserProfile

fun UserProfileEntity.toDomainModel(preferences: UserPreferences): UserProfile {
    return UserProfile(
        name = name,
        email = email,
        role = role,
        isNotificationsEnabled = preferences.isNotificationsEnabled,
        isDarkMode = preferences.isDarkMode,
        soundEnabled = preferences.soundEnabled
    )
}

fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = "primary_user",
        name = name,
        email = email,
        role = role
    )
}
