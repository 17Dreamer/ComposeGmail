package com.dreamtech.compose.gmail.core.data.repository

import com.dreamtech.compose.gmail.core.data.dao.MessageDao
import com.dreamtech.compose.gmail.core.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(private val messageDao: MessageDao) :
    MessageRepository {

    override suspend fun getMessagesByThreadId(threadId: String): List<Message> {
        return withContext(Dispatchers.IO) {
            messageDao.getMessagesByThreadId(threadId).map {
                Message.from(it)
            }
        }
    }

    override suspend fun getMessage(messageId: String): Message? {
        return withContext(Dispatchers.IO) {
            messageDao.getMessage(messageId)?.let {
                Message.from(it)
            }

        }
    }
}