package com.dreamtech.compose.gmail.di

import com.dreamtech.compose.gmail.core.data.dao.AccountDao
import com.dreamtech.compose.gmail.core.data.dao.LabelDao
import com.dreamtech.compose.gmail.core.data.dao.MessageDao
import com.dreamtech.compose.gmail.core.data.dao.ThreadDao
import com.dreamtech.compose.gmail.core.data.repository.AccountRepository
import com.dreamtech.compose.gmail.core.data.repository.AccountRepositoryImpl
import com.dreamtech.compose.gmail.core.data.repository.LabelsRepository
import com.dreamtech.compose.gmail.core.data.repository.LabelsRepositoryImpl
import com.dreamtech.compose.gmail.core.data.repository.MessageRepository
import com.dreamtech.compose.gmail.core.data.repository.MessageRepositoryImpl
import com.dreamtech.compose.gmail.core.data.repository.ThreadsRepository
import com.dreamtech.compose.gmail.core.data.repository.ThreadsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object GmailAppModule {

    @Provides
    fun providesAccountRepository(
        accountDao: AccountDao
    ): AccountRepository = AccountRepositoryImpl(accountDao)

    @Provides
    fun providesLabelsRepository(
        labelDao: LabelDao
    ): LabelsRepository = LabelsRepositoryImpl(labelDao)

    @Provides
    fun providesThreadsRepository(
        threadsDao: ThreadDao,
        messageDao: MessageDao
    ): ThreadsRepository = ThreadsRepositoryImpl(threadsDao, messageDao)

    @Provides
    fun providesMessagesRepository(
        messageDao: MessageDao
    ): MessageRepository = MessageRepositoryImpl(messageDao)
}