package com.lesli.mobile.data.local

import com.lesli.mobile.data.model.ChatMessage
import com.lesli.mobile.data.model.Conversation
import com.lesli.mobile.data.model.TaskItem
import com.lesli.mobile.data.model.TaskPriority

object DemoData {

    fun getInitialConversations(): List<Conversation> = listOf(
        Conversation(
            id = "conv_1",
            title = "Planificación del día",
            lastMessage = "Perfecto, he organizado tus 3 tareas prioritarias para hoy.",
            dateFormatted = "Hoy, 09:30",
            messageCount = 4
        ),
        Conversation(
            id = "conv_2",
            title = "Estructura de API FastAPI",
            lastMessage = "Te aconsejo separar los esquemas Pydantic en su propio módulo.",
            dateFormatted = "Ayer",
            messageCount = 8
        ),
        Conversation(
            id = "conv_3",
            title = "Ideas para Lesli Mobile",
            lastMessage = "Podemos añadir un resumen diario de productividad en la pantalla de Inicio.",
            dateFormatted = "18 Feb",
            messageCount = 6
        )
    )

    fun getInitialMessages(): List<ChatMessage> = listOf(
        ChatMessage(
            id = "msg_1",
            conversationId = "conv_1",
            text = "¡Hola Bernardo! Soy Lesli, tu asistente personal inteligente. ¿En qué te puedo inspirar u organizar hoy?",
            isUser = false,
            timestamp = System.currentTimeMillis() - 600000
        ),
        ChatMessage(
            id = "msg_2",
            conversationId = "conv_1",
            text = "¿Podrías ayudarme a organizar mis tareas clave para hoy?",
            isUser = true,
            timestamp = System.currentTimeMillis() - 300000
        ),
        ChatMessage(
            id = "msg_3",
            conversationId = "conv_1",
            text = "¡Por supuesto! Tienes 3 tareas registradas en tu lista: preparar la presentación, revisar código en Kotlin y sincronizar los endpoints locales.",
            isUser = false,
            timestamp = System.currentTimeMillis() - 100000
        )
    )

    fun getInitialTasks(): List<TaskItem> = listOf(
        TaskItem(
            id = "task_1",
            title = "Diseñar UI de Lesli Mobile",
            description = "Implementar interfaz cósmica con Jetpack Compose y tema oscuro.",
            dueDate = "Hoy",
            priority = TaskPriority.ALTA,
            isCompleted = false
        ),
        TaskItem(
            id = "task_2",
            title = "Revisar arquitectura MVVM",
            description = "Organizar repositorios, StateFlows y ViewModels.",
            dueDate = "Mañana",
            priority = TaskPriority.MEDIA,
            isCompleted = false
        ),
        TaskItem(
            id = "task_3",
            title = "Configurar navegación Navigation Compose",
            description = "Añadir barra inferior con 5 destinos.",
            dueDate = "Ayer",
            priority = TaskPriority.BAJA,
            isCompleted = true
        )
    )

    fun generateAssistantReply(userQuery: String): String {
        val lower = userQuery.lowercase()
        return when {
            lower.contains("tarea") || lower.contains("pendiente") ->
                "He revisado tus pendientes. Recuerda que puedes gestionarlos directamente en la sección Tareas o pedirme que los organice."
            lower.contains("hola") || lower.contains("saludo") ->
                "¡Hola! Estoy lista para acompañarte en lo que necesites hoy. ¿Qué deseas coordinar?"
            lower.contains("backend") || lower.contains("fastapi") || lower.contains("api") ->
                "El frontend está preparado para conectarse con la API de FastAPI. Por ahora nos encontramos en modo local de demostración."
            lower.contains("gracias") ->
                "¡Con gusto! Siempre estoy aquí para ayudarte."
            else ->
                "He recibido tu consulta sobre \"$userQuery\". He registrado el mensaje en tu conversación de Lesli Mobile."
        }
    }
}
