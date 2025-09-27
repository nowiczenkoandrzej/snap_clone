package com.an.core_editor.presentation

import androidx.compose.ui.geometry.Offset

data class UiStickerModel(
    override val rotationAngle: Float,
    override val scale: Float,
    override val alpha: Float,
    override val position: Offset,
    val stickerPath: String,
): UiElement {
    override fun center(): Offset {

        return Offset(
            x = position.x + 314f,
            y = position.y + 314f
        )
    }
}