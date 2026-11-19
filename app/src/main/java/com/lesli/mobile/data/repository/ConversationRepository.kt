package com.lesli.mobile.data.repository

import com.lesli.mobile.data.local.dao.ConversationDao
import com.lesli.mobile.data.mapper.toDomainModel
import com.lesli.mobile.data.model.Conversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ConversationRepository {
    val conversations: Flow<List<Conversation>>
    suspend fun deleteConversation(id: String)
}

class ConversationRepositoryImpl(
    private val conversationDao: ConversationDao
) : ConversationRepository {

    override val conversations: Flow<List<Conversation>> = conversationDao.getAllConversations().map { entities ->
        entities.map { it.toDomainModel() }
    }

    override suspend fun deleteConversation(id: String) {
        conversationDao.deleteConversationById(id)
    }
}
