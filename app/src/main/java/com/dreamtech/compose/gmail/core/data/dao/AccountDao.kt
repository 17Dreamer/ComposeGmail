package com.dreamtech.compose.gmail.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.dreamtech.compose.gmail.core.data.model.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert
    fun insertAccount(account: AccountEntity)

    @Transaction
    @Query(value = "SELECT * FROM account")
    fun getAccountList(): List<AccountEntity>

    @Query(value = "SELECT * FROM account WHERE isMainAccount = 1")
    fun getMainAccount(): AccountEntity?

    @Query(value = "SELECT * FROM account WHERE isMainAccount = 1")
    fun getMainAccountAsFlow(): Flow<AccountEntity>
}