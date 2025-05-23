package com.an.facefilters.canvas.domain.model

import androidx.compose.ui.geometry.Offset

interface Layer {
    val rotationAngle: Float
    val scale: Float
    val p1: Offset
    val alpha: Float

    fun transform(
        scale: Float,
        rotation: Float,
        offset: Offset
    ): Layer

    fun pivot(): Offset


}