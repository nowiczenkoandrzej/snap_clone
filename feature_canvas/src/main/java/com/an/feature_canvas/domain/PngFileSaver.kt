package com.an.feature_canvas.domain

import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.presentation.UiElement

interface PngFileSaver {
    suspend fun saveImage(
        elements: List<UiElement>,
        canvasWidth: Int,
        canvasHeight: Int,
    )
}