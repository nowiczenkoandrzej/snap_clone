package com.an.core_editor.domain

import com.an.core_editor.domain.model.DomainElement

data class EditorState(
    val elements: List<DomainElement> = emptyList(),
    val selectedElementIndex: Int? = null
)
