package com.dreamtech.compose.gmail.core.data


import com.dreamtech.compose.gmail.core.data.dao.AccountDao
import com.dreamtech.compose.gmail.core.data.dao.LabelDao
import com.dreamtech.compose.gmail.core.data.dao.MessageDao
import com.dreamtech.compose.gmail.core.data.dao.ThreadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesAccountDao(
        database: GmailDatabase,
    ): AccountDao = database.accountDao()

    @Provides
    fun providesLabelDao(
        database: GmailDatabase,
    ): LabelDao = database.labelDao()

    @Provides
    fun providesMessageDao(
        database: GmailDatabase,
    ): MessageDao = database.messageDao()

    @Provides
    fun providesThreadDao(
        database: GmailDatabase,
    ): ThreadDao = database.threadDao()

}
