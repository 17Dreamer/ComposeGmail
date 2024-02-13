package com.dreamtech.compose.gmail.core.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesGmailDatabase(
        @ApplicationContext context: Context,
    ): GmailDatabase = Room.databaseBuilder(
        context,
        GmailDatabase::class.java,
        "compose-gmail",
    ).createFromAsset("compose-gmail.db").build()
}
