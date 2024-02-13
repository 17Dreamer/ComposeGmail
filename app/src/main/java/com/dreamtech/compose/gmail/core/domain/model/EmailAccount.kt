package com.dreamtech.compose.gmail.core.domain.model

import com.dreamtech.compose.gmail.core.data.model.AccountEntity


data class EmailAccount(
    val address: String,
    val displayName: String,
    val isMainAccount: Boolean = false,
){
    companion object {
        fun from(entity: AccountEntity): EmailAccount {
            return EmailAccount(entity.address, entity.displayName, entity.isMainAccount)
        }
    }
}
