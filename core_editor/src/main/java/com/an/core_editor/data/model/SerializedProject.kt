package com.an.core_editor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializedProject(
    val elements: List<SerializedElement> = emptyList(),
    val aspectRatio: Float = 3/4f,
    val undos: List<SerializedEditorState> = emptyList()
)