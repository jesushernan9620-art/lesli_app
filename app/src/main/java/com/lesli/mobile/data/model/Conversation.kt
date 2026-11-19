package com.lesli.mobile.data.model

data class
Conversation(
    val id: String,
    val title: String,
    val lastMessage: String,
    val dateFormatted: String,
    val messageCount: Int = 1
)
