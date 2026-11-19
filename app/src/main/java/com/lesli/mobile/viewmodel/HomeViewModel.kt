package com.lesli.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesli.mobile.data.model.Conversation
import com.lesli.mobile.data.model.TaskItem
import com.lesli.mobile.data.model.UserProfile
import com.lesli.mobile.data.repository.AssistantRepository
import com.lesli.mobile.data.repository.TaskRepository
import com.lesli.mobile.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    assistantRepo: AssistantRepository,
    taskRepo: TaskRepository,
    userRepo: UserPreferencesRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = userRepo.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile()
    )

    val tasks: StateFlow<List<TaskItem>> = taskRepo.tasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val conversations: StateFlow<List<Conversation>> = assistantRepo.conversations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
