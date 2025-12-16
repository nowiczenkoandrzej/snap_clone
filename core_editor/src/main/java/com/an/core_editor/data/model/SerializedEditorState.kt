package com.an.core_editor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class  SerializedEditorState(
    val elements: List<SerializedElement> = emptyList(),
    val selectedElementIndex: Int? = null
)
