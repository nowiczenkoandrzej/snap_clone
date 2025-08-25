package com.an.core_editor.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

data class UiTextModel(
    override val rotationAngle: Float,
    override val scale: Float,
    override val alpha: Float,
    override val position: Offset,
    val text: String,
    val fontSize: Float = 60f,
    val fontColor: Color = Color.Black,
    val fontItem: FontItem = FontItem(fontFamily = FontFamily.Default, "Default")
): UiElement {
    override fun center(): Offset {
        val height = fontSize
        val width = text.length * height * 0.6f

        return Offset(
            x = position.x + (width / 2f * scale * 0.5f),
            y = position.y + (height / 2f * scale * 0.5f)
        )
    }
}
