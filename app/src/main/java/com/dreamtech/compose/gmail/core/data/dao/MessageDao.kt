package com.dreamtech.compose.gmail.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dreamtech.compose.gmail.core.data.model.MessageEntity

@Dao
interface MessageDao {

    @Insert
    fun insert(messageEntity: MessageEntity)

    @Query("SELECT * FROM message WHERE messageId =:messageId")
    fun getMessage(messageId: String): MessageEntity?

    @Query("SELECT * FROM message WHERE thread =:threadId")
    fun getMessagesByThreadId(threadId: String):List<MessageEntity>


}