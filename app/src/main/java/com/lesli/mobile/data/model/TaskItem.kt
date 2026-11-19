package com.lesli.mobile.data.model

import java.util.UUID

enum class TaskPriority {
    ALTA, MEDIA, BAJA
}

data class TaskItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val dueDate: String = "Hoy",
    val priority: TaskPriority = TaskPriority.MEDIA,
    val isCompleted: Boolean = false
)
