package com.an.facefilters.canvas.domain.model

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

data class Img(
    override val p1: Offset = Offset.Zero,
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    override val alpha: Float = 1f,
    val bitmap: Bitmap,
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

    override fun pivot(): Offset {

        return Offset(
            x = p1.x + (bitmap.width / 2),
            y = p1.y + bitmap.height / 2f
        )

    }

    override fun setAlpha(alpha: Float): Layer {
        return this.copy(
            alpha = alpha
        )
    }


}
