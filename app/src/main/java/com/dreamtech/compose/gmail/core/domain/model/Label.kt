package com.dreamtech.compose.gmail.core.domain.model

import com.dreamtech.compose.gmail.core.data.model.LabelEntity


data class Label(
    val id: Int,
    val name: String,
) {
    companion object {
        fun from(labelEntity: LabelEntity): Label {
            return Label(id = labelEntity.labelId, name = labelEntity.name)
        }
    }
}
