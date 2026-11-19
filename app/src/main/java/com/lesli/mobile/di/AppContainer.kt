package com.lesli.mobile.di

import android.content.Context
import com.lesli.mobile.core.database.LesliDatabase
import com.lesli.mobile.core.datastore.UserPreferencesDataStore
import com.lesli.mobile.data.repository.AssistantRepository
import com.lesli.mobile.data.repository.AssistantRepositoryImpl
import com.lesli.mobile.data.repository.TaskRepository
import com.lesli.mobile.data.repository.TaskRepositoryImpl
import com.lesli.mobile.data.repository.UserPreferencesRepository
import com.lesli.mobile.data.repository.UserPreferencesRepositoryImpl

interface AppContainer {
    val taskRepository: TaskRepository
    val assistantRepository: AssistantRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class AppContainerImpl(private val context: Context) : AppContainer {

    private val database: LesliDatabase by lazy {
        LesliDatabase.getInstance(context)
    }

    private val userPreferencesDataStore: UserPreferencesDataStore by lazy {
        UserPreferencesDataStore(context)
    }

    override val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(database.taskDao())
    }

    override val assistantRepository: AssistantRepository by lazy {
        AssistantRepositoryImpl(database.conversationDao(), database.chatMessageDao())
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(database.userProfileDao(), userPreferencesDataStore)
    }
}
