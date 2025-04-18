package com.an.facefilters.canvas.domain.model

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset

data class Img(
    override val p1: Offset = Offset.Zero,
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    val bitmap: Bitmap
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

    override fun containsTouchPoint(offset: Offset): Boolean {

        val width = bitmap.width * scale
        val height = bitmap.height * scale

        val result = (offset.x > p1.x && offset.x < p1.x + width && offset.y > p1.y && offset.y < p1.y + height)

        Log.d("TAG", "CanvasScreen containsTouchPoint: $result")
        return result
    }


}
