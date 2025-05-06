package com.an.facefilters.canvas.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp

data class TextModel(
    val text: String,
    val textStyle: TextStyle,
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    override val p1: Offset = Offset.Zero
): Layer {
    override fun transform(scale: Float, rotation: Float, offset: Offset): Layer {
        return this
    }

    override fun pivot(): Offset {
        val height = (textStyle.fontSize.value / 2)
        val width = (textStyle.fontSize.value / 2 * text.length)

        return Offset(
            x = 100f,
            y = 100f
        )
    }

}
