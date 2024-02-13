package com.dreamtech.compose.gmail.core.domain

import com.dreamtech.compose.gmail.core.data.repository.MessageRepository
import com.dreamtech.compose.gmail.core.data.repository.ThreadsRepository
import com.dreamtech.compose.gmail.core.domain.model.EmailThread
import com.dreamtech.compose.gmail.core.domain.model.Message
import javax.inject.Inject

class GetThreadUseCase @Inject constructor(
    private val threadsRepository: ThreadsRepository,
) {
    suspend operator fun invoke(threadId: String): EmailThread? {
        return threadsRepository.getThread(threadId)
    }
}