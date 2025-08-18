package com.an.core_editor.presentation

data class EditorUiState(
    val elements: List<UiElement> = emptyList(),
    val selectedElementIndex: Int? = null
)
