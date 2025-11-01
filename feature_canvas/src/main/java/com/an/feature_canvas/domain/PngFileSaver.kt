package com.an.feature_canvas.domain

import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel

interface PngFileSaver {
    suspend fun saveImage(
        elements: List<DomainElement>,
        canvasWidth: Int,
        canvasHeight: Int,
    )
}