package com.an.facefilters.canvas.domain

import androidx.compose.ui.geometry.Offset

sealed interface CanvasAction {

    data class TransformLayer(
        val scale: Float,
        val rotation: Float,
        val offset: Offset
    )


}