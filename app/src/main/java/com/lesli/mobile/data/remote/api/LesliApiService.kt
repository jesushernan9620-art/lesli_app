package com.lesli.mobile.data.remote.api

/**
 * Outline for future FastAPI integration on Render / Local host.
 * FastAPI endpoints will handle session cookies, tasks sync, and chat streaming.
 */
interface LesliApiService {
    // Suspend functions for FastAPI endpoints to be implemented in network phase:
    // suspend fun syncTasks(tasks: List<TaskDto>): Response<List<TaskDto>>
    // suspend fun sendChatMessage(query: String): Response<ChatResponseDto>
    // suspend fun getHistory(): Response<List<ConversationDto>>
}
