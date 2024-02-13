package com.dreamtech.compose.gmail.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "thread",
)
data class ThreadEntity(
    @PrimaryKey
    val threadId: String,
    val account: String,
    val title: String,
    val subject: String,
    val snippet: String,
    val messageCount: Int,
    val date: Long,
    val isStarred: Boolean = false,
    val isLabeled: Boolean = false,
)