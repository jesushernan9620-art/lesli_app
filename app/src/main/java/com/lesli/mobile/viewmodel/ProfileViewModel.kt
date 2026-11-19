package com.lesli.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesli.mobile.data.model.UserProfile
import com.lesli.mobile.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepo: UserPreferencesRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = userRepo.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile()
    )

    private val _isEditDialogVisible = MutableStateFlow(false)
    val isEditDialogVisible: StateFlow<Boolean> = _isEditDialogVisible.asStateFlow()

    private val _isLogoutDialogVisible = MutableStateFlow(false)
    val isLogoutDialogVisible: StateFlow<Boolean> = _isLogoutDialogVisible.asStateFlow()

    fun openEditDialog() {
        _isEditDialogVisible.value = true
    }

    fun closeEditDialog() {
        _isEditDialogVisible.value = false
    }

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            userRepo.updateProfile(name, email)
            closeEditDialog()
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            userRepo.toggleNotifications(enabled)
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            userRepo.toggleDarkMode(enabled)
        }
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            userRepo.toggleSound(enabled)
        }
    }

    fun openLogoutDialog() {
        _isLogoutDialogVisible.value = true
    }

    fun closeLogoutDialog() {
        _isLogoutDialogVisible.value = false
    }

    fun logout() {
        viewModelScope.launch {
            closeLogoutDialog()
            userRepo.updateProfile("Bernardo", "bernardo@ejemplo.com")
        }
    }
}
