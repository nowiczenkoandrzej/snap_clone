package com.an.facefilters.canvas.domain.model

import androidx.compose.ui.geometry.Offset

data class Img(
    override val p1: Offset = Offset.Zero,
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f
): Layer {
    override fun transform(scale: Float, rotation: Float, offset: Offset): Layer {

        var newScale = this.scale * scale
        newScale = newScale.coerceIn(0.1f, 3f)

        return this.copy(
            scale = newScale,
            rotationAngle = this.rotationAngle + rotation,
            p1 = p1.plus(offset)
        )

    }


}
