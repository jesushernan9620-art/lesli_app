package com.lesli.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesli.mobile.data.model.TaskItem
import com.lesli.mobile.data.model.TaskPriority
import com.lesli.mobile.data.repository.TaskRepository
import com.lesli.mobile.ui.components.TaskFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class TaskViewModel(
    private val taskRepo: TaskRepository
) : ViewModel() {

    val tasks: StateFlow<List<TaskItem>> = taskRepo.tasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedFilter = MutableStateFlow(TaskFilter.TODAS)
    val selectedFilter: StateFlow<TaskFilter> = _selectedFilter.asStateFlow()

    private val _taskToEdit = MutableStateFlow<TaskItem?>(null)
    val taskToEdit: StateFlow<TaskItem?> = _taskToEdit.asStateFlow()

    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible: StateFlow<Boolean> = _isDialogVisible.asStateFlow()

    fun selectFilter(filter: TaskFilter) {
        _selectedFilter.value = filter
    }

    fun openCreateDialog() {
        _taskToEdit.value = null
        _isDialogVisible.value = true
    }

    fun openEditDialog(task: TaskItem) {
        _taskToEdit.value = task
        _isDialogVisible.value = true
    }

    fun closeDialog() {
        _isDialogVisible.value = false
        _taskToEdit.value = null
    }

    fun saveTask(title: String, description: String, dueDate: String, priority: TaskPriority) {
        val currentEdit = _taskToEdit.value
        viewModelScope.launch {
            if (currentEdit != null) {
                val updated = currentEdit.copy(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    priority = priority
                )
                taskRepo.updateTask(updated)
            } else {
                val newTask = TaskItem(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    priority = priority,
                    isCompleted = false
                )
                taskRepo.addTask(newTask)
            }
            closeDialog()
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        viewModelScope.launch {
            taskRepo.toggleTaskCompletion(taskId)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepo.deleteTask(taskId)
        }
    }
}
