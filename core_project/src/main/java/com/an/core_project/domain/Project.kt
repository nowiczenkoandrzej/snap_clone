package com.an.core_project.domain

import com.an.core_editor.domain.EditorState
import com.an.core_editor.domain.model.DomainElement

data class Project(
    val id: Int,
    val elements: List<DomainElement> = emptyList(),
    val aspectRatio: Float = 3/4f,
    val undos: List<EditorState> = emptyList(),
    val lastChange: Long,
    val thumbNail: String? = null
)
