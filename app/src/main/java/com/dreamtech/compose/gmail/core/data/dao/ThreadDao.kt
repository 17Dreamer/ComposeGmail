package com.dreamtech.compose.gmail.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dreamtech.compose.gmail.core.data.model.ThreadEntity
import com.dreamtech.compose.gmail.core.domain.model.Label
import kotlinx.coroutines.flow.Flow

@Dao
interface ThreadDao {

    @Transaction
    @Query(value = "SELECT * FROM thread, threadlabelcrossref WHERE account LIKE :account AND thread.threadId = threadlabelcrossref.threadId AND threadlabelcrossref.labelId IN (:labels)")
    fun getAllThreadsByLabel(account: String, labels: List<Int>): Flow<List<ThreadEntity>>

    @Transaction
    @Query(value = "SELECT * FROM thread WHERE account LIKE :account")
    fun getAllThreads(account: String): Flow<List<ThreadEntity>>

    @Insert
    fun insertThread(entity: ThreadEntity)

    @Query(value = "SELECT * FROM thread WHERE threadId LIKE :threadId")
    fun getThread(threadId: String): ThreadEntity?

    @Update
    fun update(threadEntity: ThreadEntity)
}