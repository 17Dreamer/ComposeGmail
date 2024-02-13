package com.dreamtech.compose.gmail.core.domain

import com.dreamtech.compose.gmail.core.data.repository.AccountRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(email: String, password: String): Boolean {

        //TODO check and validate the Gmail account via Api

        //Add the account to local DB
        accountRepository.addAccount(email, password)
        return true
    }
}