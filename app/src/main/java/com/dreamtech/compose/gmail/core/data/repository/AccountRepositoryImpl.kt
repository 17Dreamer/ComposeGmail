package com.dreamtech.compose.gmail.core.data.repository

import android.util.Log
import com.dreamtech.compose.gmail.core.data.dao.AccountDao
import com.dreamtech.compose.gmail.core.data.model.AccountEntity
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountDao: AccountDao) :
    AccountRepository {

    override suspend fun getAccounts(): List<EmailAccount> = withContext(Dispatchers.IO) {
        return@withContext accountDao.getAccountList().map {
            EmailAccount.from(it)
        }
    }

    override suspend fun getMainAccount(): EmailAccount? = withContext(Dispatchers.IO) {
        return@withContext accountDao.getMainAccount()?.let{
            Log.i("AccountRepositoryImpl", "getMainAccount = $it")
            EmailAccount.from(it)
        }
    }

    override suspend fun getMainAccountAsFlow(): Flow<EmailAccount> {
        return accountDao.getMainAccountAsFlow().map{
            EmailAccount.from(it)
        }
    }

    override suspend fun addAccount(email: String, password: String)  = withContext(Dispatchers.IO) {
        val isMainAccount = accountDao.getMainAccount()?.let{false}?:run{true}
        val accountEntity = AccountEntity(
            address = email,
            displayName = email,
            isMainAccount = isMainAccount
        )
        accountDao.insertAccount(accountEntity)
    }
}