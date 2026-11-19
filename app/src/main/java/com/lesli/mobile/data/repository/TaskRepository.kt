package com.lesli.mobile.data.repository

import com.lesli.mobile.data.local.DemoData
import com.lesli.mobile.data.local.dao.TaskDao
import com.lesli.mobile.data.mapper.toDomainModel
import com.lesli.mobile.data.mapper.toEntity
import com.lesli.mobile.data.model.TaskItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface TaskRepository {
    val tasks: Flow<List<TaskItem>>
    suspend fun addTask(task: TaskItem)
    suspend fun updateTask(task: TaskItem)
    suspend fun toggleTaskCompletion(taskId: String)
    suspend fun deleteTask(taskId: String)
}

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    init {
        // Pre-populate database with initial demo data if database is empty
        CoroutineScope(Dispatchers.IO).launch {
            if (taskDao.getAllTasks().first().isEmpty()) {
                val initialEntities = DemoData.getInitialTasks().map { it.toEntity() }
                taskDao.insertTasks(initialEntities)
            }
        }
    }

    override val tasks: Flow<List<TaskItem>> = taskDao.getAllTasks().map { entities ->
        entities.map { it.toDomainModel() }
    }

    override suspend fun addTask(task: TaskItem) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: TaskItem) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun toggleTaskCompletion(taskId: String) {
        val existing = taskDao.getTaskById(taskId)
        if (existing != null) {
            val updated = existing.copy(
                isCompleted = !existing.isCompleted,
                updatedAt = System.currentTimeMillis()
            )
            taskDao.updateTask(updated)
        }
    }

    override suspend fun deleteTask(taskId: String) {
        taskDao.deleteTaskById(taskId)
    }
}
