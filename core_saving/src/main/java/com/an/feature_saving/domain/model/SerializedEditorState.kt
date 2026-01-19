package com.an.feature_saving.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class  SerializedEditorState(
    val elements: List<SerializedElement> = emptyList(),
    val selectedElementIndex: Int? = null
)
