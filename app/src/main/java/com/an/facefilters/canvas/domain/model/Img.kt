package com.an.facefilters.canvas.domain.model

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset

data class Img(
    override val p1: Offset = Offset.Zero,
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    override val alpha: Float = 1f,
    val bitmap: Bitmap,
    val originalBitmap: Bitmap,
    val currentFilter: String = "Original"
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

        return Offset(
            x = p1.x + (bitmap.width / 2),
            y = p1.y + bitmap.height / 2f
        )

    }

    override fun setAlpha(alpha: Float): Element {
        return this.copy(
            alpha = alpha
        )
    }


}
