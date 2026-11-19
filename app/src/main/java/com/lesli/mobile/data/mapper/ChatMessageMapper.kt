package com.lesli.mobile.data.mapper

import com.lesli.mobile.data.local.entity.ChatMessageEntity
import com.lesli.mobile.data.model.ChatMessage

fun ChatMessageEntity.toDomainModel(): ChatMessage {
    return ChatMessage(
        id = id,
        conversationId = conversationId,
        text = text,
        isUser = isUser,
        timestamp = timestamp
    )
}

fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        id = id,
        conversationId = conversationId,
        text = text,
        isUser = isUser,
        timestamp = timestamp
    )
}
