package com.an.facefilters.canvas.domain.model

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle

data class TextModel(
    val text: String,
    val textStyle: TextStyle,
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    override val p1: Offset = Offset.Zero,
    override val alpha: Float = 1f
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

        val height = textStyle.fontSize.value
        val width = text.length * height * 0.6f

        return Offset(
            x = p1.x + width / 2f,
            y = p1.y + height / 2f
        )
    }

    override fun setAlpha(alpha: Float): Element {
        return this.copy(
            alpha = alpha
        )
    }

}
