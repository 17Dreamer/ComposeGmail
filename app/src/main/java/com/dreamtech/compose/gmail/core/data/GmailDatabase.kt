package com.dreamtech.compose.gmail.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dreamtech.compose.gmail.core.data.dao.AccountDao
import com.dreamtech.compose.gmail.core.data.dao.LabelDao
import com.dreamtech.compose.gmail.core.data.dao.MessageDao
import com.dreamtech.compose.gmail.core.data.dao.ThreadDao
import com.dreamtech.compose.gmail.core.data.model.AccountEntity
import com.dreamtech.compose.gmail.core.data.model.LabelEntity
import com.dreamtech.compose.gmail.core.data.model.MessageEntity
import com.dreamtech.compose.gmail.core.data.model.ThreadEntity
import com.dreamtech.compose.gmail.core.data.model.ThreadLabelCrossRef

@Database(
    entities = [
        AccountEntity::class,
        LabelEntity::class,
        ThreadEntity::class,
        MessageEntity::class,
        ThreadLabelCrossRef::class
    ],
    version = 1,
    exportSchema = true,
)

abstract class GmailDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun labelDao(): LabelDao
    abstract fun threadDao(): ThreadDao
    abstract fun messageDao(): MessageDao
}
