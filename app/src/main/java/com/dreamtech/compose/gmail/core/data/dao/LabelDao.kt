package com.dreamtech.compose.gmail.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.dreamtech.compose.gmail.core.data.model.LabelEntity

@Dao
interface LabelDao {

    @Transaction
    @Query(value = "SELECT * FROM label WHERE account =:account")
    fun getLabelsByAccount(account: String): List<LabelEntity>

    @Insert
    fun insertLabel(entity: LabelEntity)
}