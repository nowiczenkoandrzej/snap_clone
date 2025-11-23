package com.an.feature_canvas.domain

import com.an.core_editor.presentation.model.UiElement

interface PngFileSaver {
    suspend fun saveImage(
        elements: List<UiElement>,
        canvasWidth: Int,
        canvasHeight: Int,
    )
}