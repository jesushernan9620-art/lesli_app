package com.lesli.mobile

import com.lesli.mobile.core.common.SyncStatus
import com.lesli.mobile.data.local.entity.ConversationEntity
import com.lesli.mobile.data.local.entity.TaskEntity
import com.lesli.mobile.data.mapper.toDomainModel
import com.lesli.mobile.data.mapper.toEntity
import com.lesli.mobile.data.model.TaskItem
import com.lesli.mobile.data.model.TaskPriority
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalPersistenceUnitTest {

    @Test
    fun taskEntity_toDomainModel_conversion_isCorrect() {
        val entity = TaskEntity(
            id = "task_100",
            title = "Probar Room SQLite",
            description = "Verificar persistencia local de tareas",
            dueDate = "Hoy",
            priority = "ALTA",
            isCompleted = false,
            syncStatus = SyncStatus.PENDING.name
        )

        val domain = entity.toDomainModel()

        assertEquals("task_100", domain.id)
        assertEquals("Probar Room SQLite", domain.title)
        assertEquals(TaskPriority.ALTA, domain.priority)
        assertFalse(domain.isCompleted)
    }

    @Test
    fun taskItem_toEntity_conversion_isCorrect() {
        val item = TaskItem(
            id = "task_101",
            title = "Sincronización Local First",
            description = "Preparar esquema para FastAPI",
            dueDate = "Mañana",
            priority = TaskPriority.MEDIA,
            isCompleted = true
        )

        val entity = item.toEntity()

        assertEquals("task_101", entity.id)
        assertEquals("Sincronización Local First", entity.title)
        assertEquals("MEDIA", entity.priority)
        assertTrue(entity.isCompleted)
        assertEquals(SyncStatus.PENDING.name, entity.syncStatus)
    }

    @Test
    fun conversationEntity_properties_areCorrect() {
        val conv = ConversationEntity(
            id = "conv_200",
            title = "Conversación Cósmica",
            lastMessage = "Hola Lesli",
            dateFormatted = "Ahora",
            messageCount = 2
        )

        assertEquals("conv_200", conv.id)
        assertEquals("Conversación Cósmica", conv.title)
        assertEquals(2, conv.messageCount)
    }
}
