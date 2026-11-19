package com.lesli.mobile.domain.usecase.task

import com.lesli.mobile.data.model.TaskItem
import com.lesli.mobile.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskItem>> = repository.tasks
}

class AddTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: TaskItem) = repository.addTask(task)
}

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: TaskItem) = repository.updateTask(task)
}

class ToggleTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String) = repository.toggleTaskCompletion(taskId)
}

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String) = repository.deleteTask(taskId)
}
