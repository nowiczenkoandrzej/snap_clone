package com.an.feature_image_editing.presentation.util

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize

fun Bitmap.cropToRect(srcRect: Rect, viewSize: IntSize): Bitmap {
    val scaleX = width.toFloat() / viewSize.width
    val scaleY = height.toFloat() / viewSize.height

    val x = (srcRect.left * scaleX).toInt().coerceIn(0, width)
    val y = (srcRect.top * scaleY).toInt().coerceIn(0, height)
    val width = (srcRect.width * scaleX).toInt().coerceIn(0, width - x)
    val height = (srcRect.height * scaleY).toInt().coerceIn(0, height - y)

    return Bitmap.createBitmap(this, x, y, width, height)
}