package com.dreamtech.compose.gmail.core.data.repository

import com.dreamtech.compose.gmail.core.domain.model.Message

interface MessageRepository {

    suspend fun getMessagesByThreadId(threadId: String): List<Message>

    suspend fun getMessage(messageId: String): Message?
}