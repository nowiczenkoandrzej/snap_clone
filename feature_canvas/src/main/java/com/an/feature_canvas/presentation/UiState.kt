package com.an.feature_canvas.presentation

import com.an.core_editor.presentation.model.UiElement
import com.an.core_project.domain.Project

data class UiState(
    val id: Long? = null,
    val elements: List<UiElement> = emptyList(),
    val aspectRatio: Float = 3/4f,
    val selectedElementIndex: Int? = null
)

fun Project.ToUICanvasState(): UiState {
    return UiState(
        id = this.id,
        elements
    )
}