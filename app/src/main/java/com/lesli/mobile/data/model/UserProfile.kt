package com.lesli.mobile.data.model

data class UserProfile(
    val name: String = "Bernardo",
    val email: String = "bernardo@ejemplo.com",
    val role: String = "Usuario Lesli Mobile",
    val isNotificationsEnabled: Boolean = true,
    val isDarkMode: Boolean = true,
    val soundEnabled: Boolean = true
)
