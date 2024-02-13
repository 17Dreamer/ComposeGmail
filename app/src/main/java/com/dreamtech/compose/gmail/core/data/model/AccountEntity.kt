package com.dreamtech.compose.gmail.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "account",
)
data class AccountEntity(

    @PrimaryKey
    val address: String,

    val displayName: String,

    val isMainAccount: Boolean,
)