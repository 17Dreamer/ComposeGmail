package com.dreamtech.compose.gmail.core.domain

import com.dreamtech.compose.gmail.core.data.repository.AccountRepository
import com.dreamtech.compose.gmail.core.data.repository.ThreadsRepository
import com.dreamtech.compose.gmail.core.domain.model.EmailThread
import com.dreamtech.compose.gmail.core.domain.model.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetThreadListUseCase @Inject constructor(
    private val threadsRepository: ThreadsRepository,
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(labels: List<Int> = emptyList<Int>()): Flow<List<EmailThread>> {
        val mainAccount = accountRepository.getMainAccount()
        return mainAccount?.let { threadsRepository.getAllThreads(it, labels) }
            ?: run { flow<List<EmailThread>> {} }.flowOn(Dispatchers.IO)
    }
}