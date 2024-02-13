package com.dreamtech.compose.gmail.core.data.model

import androidx.room.Entity

@Entity(primaryKeys = ["threadId", "labelId"])
data class ThreadLabelCrossRef(
    val threadId: String,
    val labelId: Int
)
