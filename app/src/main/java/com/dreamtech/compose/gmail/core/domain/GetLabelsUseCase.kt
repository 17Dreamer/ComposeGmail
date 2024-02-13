package com.dreamtech.compose.gmail.core.domain

import com.dreamtech.compose.gmail.core.data.repository.LabelsRepository
import com.dreamtech.compose.gmail.core.domain.model.Label
import javax.inject.Inject

class GetLabelsUseCase @Inject constructor(
    private val labelsRepository: LabelsRepository,
) {
    suspend operator fun invoke(account: String): List<Label> {
        return labelsRepository.getLabelsByAccount(account)
    }
}