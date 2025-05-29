package com.an.facefilters.canvas.domain.model

import androidx.compose.ui.geometry.Offset

interface Element {
    val rotationAngle: Float
    val scale: Float
    val p1: Offset
    val alpha: Float

    fun transform(
        scale: Float,
        rotation: Float,
        offset: Offset
    ): Element

    fun pivot(): Offset

    fun setAlpha(alpha: Float): Element


}