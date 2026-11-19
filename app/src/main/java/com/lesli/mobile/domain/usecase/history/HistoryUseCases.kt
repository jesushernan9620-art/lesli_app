package com.lesli.mobile.domain.usecase.history

import com.lesli.mobile.data.model.Conversation
import com.lesli.mobile.data.repository.AssistantRepository
import kotlinx.coroutines.flow.Flow

class GetConversationsUseCase(private val repository: AssistantRepository) {
    operator fun invoke(): Flow<List<Conversation>> = repository.conversations
}

class DeleteConversationUseCase(private val repository: AssistantRepository) {
    suspend operator fun invoke(id: String) = repository.deleteConversation(id)
}

class SelectConversationUseCase(private val repository: AssistantRepository) {
    operator fun invoke(id: String) = repository.selectConversation(id)
}
