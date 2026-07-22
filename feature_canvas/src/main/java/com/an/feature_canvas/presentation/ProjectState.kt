package com.an.feature_canvas.presentation

import com.an.core_editor.presentation.model.UiElement

data class ProjectState(
    val id: Long? = null,
    val elements: List<UiElement> = emptyList(),
    val aspectRatio: Float = 3/4f,
    val selectedElementIndex: Int? = null
)

