package com.dreamtech.compose.gmail.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "message",
)
data class MessageEntity(
    @PrimaryKey
    val messageId: String,
    val thread: String,
    val sender: String,
    val destination: String,
    val ccDestination: String,
    val bccDestination: String,
    val dateTime: Long,
    val content: String,
)