package com.dreamtech.compose.gmail.core.data.repository


import com.dreamtech.compose.gmail.core.data.dao.MessageDao
import com.dreamtech.compose.gmail.core.data.dao.ThreadDao
import com.dreamtech.compose.gmail.core.data.model.MessageEntity
import com.dreamtech.compose.gmail.core.data.model.ThreadEntity
import com.dreamtech.compose.gmail.core.domain.Result
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import com.dreamtech.compose.gmail.core.domain.model.EmailThread
import com.dreamtech.compose.gmail.core.domain.model.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ThreadsRepositoryImpl @Inject constructor(
    private val threadDao: ThreadDao,
    private val messageDao: MessageDao
) :
    ThreadsRepository {

    override fun getAllThreads(account: EmailAccount, labels: List<Int>): Flow<List<EmailThread>> {
        return if(labels.isNotEmpty()) {
            threadDao.getAllThreadsByLabel(account.address, labels).map {
                it.map { entity -> EmailThread.from(entity) }
            }
        } else {
            threadDao.getAllThreads(account.address).map {
                it.map { entity -> EmailThread.from(entity) }
            }
        }

    }

    override suspend fun getThread(threadId: String): EmailThread? {
        return withContext(Dispatchers.IO) {
            threadDao.getThread(threadId = threadId)?.let { EmailThread.from(it) }
        }
    }

    override suspend fun sendNewMail(
        thread: String?,
        account: String,
        title: String,
        destination: String,
        ccDestination: String,
        bccDestination: String,
        subject: String,
        snippet: String,
        content: String,
    ): Result {

        var existingThread : ThreadEntity? = null
        if(thread != null){
            existingThread = threadDao.getThread(thread)
        }
        val date = System.currentTimeMillis()
        val threadId: String
        if(existingThread == null) {
             threadId = UUID.randomUUID().toString()
            val threadEntity = ThreadEntity(
                threadId = threadId,
                account = account,
                title = title,
                subject = subject,
                snippet = snippet,
                messageCount = 1,
                date = date
            )
            threadDao.insertThread(threadEntity)
        }else{
            threadId = existingThread.threadId
            val threadEntity =existingThread.copy(
                title = title,
                subject = subject,
                snippet = snippet,
                messageCount = existingThread.messageCount+1,
                date = date
            )
            threadDao.update(threadEntity)
        }

        val messageId = UUID.randomUUID().toString()
        val messageEntity = MessageEntity(
            messageId = messageId,
            thread = threadId,
            sender = account,
            destination = destination,
            ccDestination = ccDestination,
            bccDestination = bccDestination,
            dateTime = date,
            content = content
        )
        messageDao.insert(messageEntity)
        return Result.Success()
    }
}