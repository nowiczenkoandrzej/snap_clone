package com.an.core_editor.presentation

import com.an.core_editor.presentation.model.UiElement

data class EditorUiState(
    val elements: List<UiElement> = emptyList(),
    val selectedElementIndex: Int? = null
)
