package com.lesli.mobile.data.mapper

import com.lesli.mobile.data.local.entity.TaskEntity
import com.lesli.mobile.data.model.TaskItem
import com.lesli.mobile.data.model.TaskPriority

fun TaskEntity.toDomainModel(): TaskItem {
    return TaskItem(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        priority = try {
            TaskPriority.valueOf(priority)
        } catch (e: Exception) {
            TaskPriority.MEDIA
        },
        isCompleted = isCompleted
    )
}

fun TaskItem.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        priority = priority.name,
        isCompleted = isCompleted
    )
}
