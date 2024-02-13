package com.dreamtech.compose.gmail.core.domain

import com.dreamtech.compose.gmail.core.data.repository.MessageRepository
import com.dreamtech.compose.gmail.core.domain.model.Message
import javax.inject.Inject

class GetThreadDetailsUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(thread: String): List<Message> {
        return messageRepository.getMessagesByThreadId(thread)
    }
}