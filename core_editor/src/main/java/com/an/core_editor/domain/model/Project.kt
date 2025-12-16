package com.an.core_editor.domain.model

import com.an.core_editor.domain.EditorState

data class Project(
    val elements: List<DomainElement> = emptyList(),
    val aspectRatio: Float = 3/4f,
    val undos: List<EditorState> = emptyList()
)
