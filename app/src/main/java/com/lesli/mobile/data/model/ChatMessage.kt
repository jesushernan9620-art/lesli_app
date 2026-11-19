package com.lesli.mobile.data.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val conversationId: String = "default",
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
