package com.dreamtech.compose.gmail.core.data.repository

import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun getAccounts(): List<EmailAccount>

    suspend fun getMainAccount(): EmailAccount?

    suspend fun getMainAccountAsFlow(): Flow<EmailAccount>

    suspend fun addAccount(email: String, password: String)
}