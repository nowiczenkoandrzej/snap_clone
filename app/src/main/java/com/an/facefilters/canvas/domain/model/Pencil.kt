package com.an.facefilters.canvas.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Pencil(
    val paths: List<PathData> = emptyList(),
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    override val p1: Offset = Offset.Zero
): Layer {
    override fun transform(scale: Float, rotation: Float, offset: Offset): Layer {
        return this
    }

    override fun containsTouchPoint(offset: Offset): Boolean {
        return false
    }

}

data class PathData(
    val color: Color,
    val path: List<Offset>
)
