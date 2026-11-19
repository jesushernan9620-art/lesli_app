package com.lesli.mobile.data.repository

import com.lesli.mobile.data.local.DemoData
import com.lesli.mobile.data.local.dao.ChatMessageDao
import com.lesli.mobile.data.local.dao.ConversationDao
import com.lesli.mobile.data.mapper.toDomainModel
import com.lesli.mobile.data.mapper.toEntity
import com.lesli.mobile.data.model.ChatMessage
import com.lesli.mobile.data.model.Conversation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

interface AssistantRepository {
    val messages: Flow<List<ChatMessage>>
    val conversations: Flow<List<Conversation>>
    val currentConversationId: StateFlow<String>

    suspend fun sendMessage(text: String)
    suspend fun startNewConversation()
    fun selectConversation(id: String)
    suspend fun deleteConversation(id: String)
}

class AssistantRepositoryImpl(
    private val conversationDao: ConversationDao,
    private val chatMessageDao: ChatMessageDao
) : AssistantRepository {

    private val _currentConversationId = MutableStateFlow("conv_1")
    override val currentConversationId: StateFlow<String> = _currentConversationId.asStateFlow()

    init {
        // Pre-populate Room database with initial demo conversations and messages if empty
        CoroutineScope(Dispatchers.IO).launch {
            if (conversationDao.getAllConversations().first().isEmpty()) {
                val initialConvs = DemoData.getInitialConversations().map { it.toEntity() }
                conversationDao.insertConversations(initialConvs)

                val initialMsgs = DemoData.getInitialMessages().map { it.toEntity() }
                chatMessageDao.insertMessages(initialMsgs)
            }
        }
    }

    override val conversations: Flow<List<Conversation>> = conversationDao.getAllConversations().map { entities ->
        entities.map { it.toDomainModel() }
    }

    override val messages: Flow<List<ChatMessage>> = chatMessageDao.getAllMessages().map { entities ->
        entities.map { it.toDomainModel() }
    }

    override suspend fun sendMessage(text: String) {
        val convId = _currentConversationId.value
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = convId,
            text = text,
            isUser = true
        )

        val replyText = DemoData.generateAssistantReply(text)
        val lesliMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = convId,
            text = replyText,
            isUser = false
        )

        chatMessageDao.insertMessage(userMsg.toEntity())
        chatMessageDao.insertMessage(lesliMsg.toEntity())

        // Update active conversation in Room
        val existingConv = conversationDao.getConversationById(convId)
        val nowFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        if (existingConv != null) {
            val updatedConv = existingConv.copy(
                lastMessage = replyText,
                dateFormatted = "Hoy, $nowFormatted",
                messageCount = existingConv.messageCount + 2,
                lastActivityAt = System.currentTimeMillis()
            )
            conversationDao.updateConversation(updatedConv)
        }
    }

    override suspend fun startNewConversation() {
        val newId = "conv_" + System.currentTimeMillis()
        val title = "Nueva conversación"
        val initialMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = newId,
            text = "¡Hola! He iniciado un nuevo espacio de conversación para ti. ¿En qué trabajaremos ahora?",
            isUser = false
        )
        val newConv = Conversation(
            id = newId,
            title = title,
            lastMessage = initialMsg.text,
            dateFormatted = "Ahora",
            messageCount = 1
        )

        conversationDao.insertConversation(newConv.toEntity())
        chatMessageDao.insertMessage(initialMsg.toEntity())
        _currentConversationId.value = newId
    }

    override fun selectConversation(id: String) {
        _currentConversationId.value = id
    }

    override suspend fun deleteConversation(id: String) {
        conversationDao.deleteConversationById(id)
        // ChatMessage CASCADE foreign key automatically deletes child messages in SQLite
        if (_currentConversationId.value == id) {
            val remaining = conversationDao.getAllConversations().first().firstOrNull()
            if (remaining != null) {
                _currentConversationId.value = remaining.id
            } else {
                startNewConversation()
            }
        }
    }
}
