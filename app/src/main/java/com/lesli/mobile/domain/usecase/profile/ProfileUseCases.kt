package com.lesli.mobile.domain.usecase.profile

import com.lesli.mobile.data.model.UserProfile
import com.lesli.mobile.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(private val repository: UserPreferencesRepository) {
    operator fun invoke(): Flow<UserProfile> = repository.userProfile
}

class UpdateUserProfileUseCase(private val repository: UserPreferencesRepository) {
    suspend operator fun invoke(name: String, email: String) = repository.updateProfile(name, email)
}
