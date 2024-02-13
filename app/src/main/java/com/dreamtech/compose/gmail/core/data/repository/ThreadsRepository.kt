package com.dreamtech.compose.gmail.core.data.repository

import com.dreamtech.compose.gmail.core.domain.Result
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import com.dreamtech.compose.gmail.core.domain.model.EmailThread
import com.dreamtech.compose.gmail.core.domain.model.Label
import kotlinx.coroutines.flow.Flow


interface ThreadsRepository {

    fun getAllThreads(account: EmailAccount, labels: List<Int>): Flow<List<EmailThread>>

    suspend fun sendNewMail(
        thread: String?,
        account: String,
        title: String,
        destination: String,
        ccDestination: String,
        bccDestination: String,
        subject: String,
        snippet: String,
        content: String,
    ): Result

    suspend fun getThread(threadId: String): EmailThread?
}