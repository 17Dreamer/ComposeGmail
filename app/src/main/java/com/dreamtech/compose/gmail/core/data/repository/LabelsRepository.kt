package com.dreamtech.compose.gmail.core.data.repository

import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import com.dreamtech.compose.gmail.core.domain.model.Label

interface LabelsRepository {

    suspend fun getLabelsByAccount(account: String):List<Label>
}