package com.an.facefilters.canvas.domain.model

import androidx.compose.ui.geometry.Offset

data class Sticker(
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    override val p1: Offset = Offset.Zero,
    override val alpha: Float = 1f,
    val svgAsset: String
): Element {
    override fun transform(scale: Float, rotation: Float, offset: Offset): Element {
        var newScale = this.scale * scale
        newScale = newScale.coerceIn(0.1f, 3f)
        return this.copy(
            scale = newScale,
            rotationAngle = this.rotationAngle + rotation,
            p1 = p1.plus(offset)
        )
    }

    override fun pivot(): Offset {
        return this.p1
    }

    override fun setAlpha(alpha: Float): Element {
        return this.copy(
            alpha = alpha
        )
    }
}