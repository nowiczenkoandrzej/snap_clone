package com.an.core_editor.data.edits

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect

data class CropImage(val cropRect: Rect): ImageEdit {
    override fun apply(input: Bitmap): Bitmap {
        val left = cropRect.left.toInt().coerceIn(0, input.width)
        val top = cropRect.top.toInt().coerceIn(0, input.height)
        val width = cropRect.width.toInt().coerceAtMost(input.width - left)
        val height = cropRect.height.toInt().coerceAtMost(input.height - top)

        return Bitmap.createBitmap(input, left, top, width, height)
    }
}