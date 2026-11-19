package com.lesli.mobile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lesli.mobile.viewmodel.AssistantViewModel
import com.lesli.mobile.viewmodel.HistoryViewModel
import com.lesli.mobile.viewmodel.HomeViewModel
import com.lesli.mobile.viewmodel.ProfileViewModel
import com.lesli.mobile.viewmodel.TaskViewModel

class LesliViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    appContainer.assistantRepository,
                    appContainer.taskRepository,
                    appContainer.userPreferencesRepository
                ) as T
            }
            modelClass.isAssignableFrom(AssistantViewModel::class.java) -> {
                AssistantViewModel(appContainer.assistantRepository) as T
            }
            modelClass.isAssignableFrom(TaskViewModel::class.java) -> {
                TaskViewModel(appContainer.taskRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(appContainer.assistantRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(appContainer.userPreferencesRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
