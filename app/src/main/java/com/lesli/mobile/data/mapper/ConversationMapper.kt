package com.lesli.mobile.data.mapper

import com.lesli.mobile.data.local.entity.ConversationEntity
import com.lesli.mobile.data.model.Conversation

fun ConversationEntity.toDomainModel(): Conversation {
    return Conversation(
        id = id,
        title = title,
        lastMessage = lastMessage,
        dateFormatted = dateFormatted,
        messageCount = messageCount
    )
}

fun Conversation.toEntity(): ConversationEntity {
    return ConversationEntity(
        id = id,
        title = title,
        lastMessage = lastMessage,
        dateFormatted = dateFormatted,
        messageCount = messageCount
    )
}
