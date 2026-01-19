package com.an.feature_saving.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializedProject(
    val elements: List<SerializedElement> = emptyList(),
    val aspectRatio: Float = 3/4f,
    val undos: List<SerializedEditorState> = emptyList()
)