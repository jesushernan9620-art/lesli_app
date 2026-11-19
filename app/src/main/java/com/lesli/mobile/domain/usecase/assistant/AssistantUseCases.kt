package com.lesli.mobile.domain.usecase.assistant

import com.lesli.mobile.data.model.ChatMessage
import com.lesli.mobile.data.repository.AssistantRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(private val repository: AssistantRepository) {
    operator fun invoke(): Flow<List<ChatMessage>> = repository.messages
}

class SendMessageUseCase(private val repository: AssistantRepository) {
    suspend operator fun invoke(text: String) = repository.sendMessage(text)
}

class StartNewConversationUseCase(private val repository: AssistantRepository) {
    suspend operator fun invoke() = repository.startNewConversation()
}
