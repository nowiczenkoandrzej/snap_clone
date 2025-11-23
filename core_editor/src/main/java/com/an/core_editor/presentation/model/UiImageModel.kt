package com.an.core_editor.presentation.model

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset

data class UiImageModel(
    override val rotationAngle: Float,
    override val scale: Float,
    override val alpha: Float,
    override val position: Offset,
    val bitmap: Bitmap?,
    val currentFilter: String = "Original",
    val version: Long = System.currentTimeMillis()
): UiElement {
    override fun center(): Offset {
        if(bitmap == null) return Offset.Zero

        return Offset(
            x = position.x + (bitmap.width / 2),
            y = position.y + bitmap.height / 2f
        )
    }
}