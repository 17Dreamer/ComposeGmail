package com.dreamtech.compose.gmail.core.data.repository

import com.dreamtech.compose.gmail.core.data.dao.LabelDao
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import com.dreamtech.compose.gmail.core.domain.model.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LabelsRepositoryImpl @Inject constructor(private val labelDao: LabelDao) : LabelsRepository {

    override suspend fun getLabelsByAccount(account: String): List<Label> = withContext(Dispatchers.IO){
        return@withContext labelDao.getLabelsByAccount(account).map {
            Label.from(it)
        }
    }
}