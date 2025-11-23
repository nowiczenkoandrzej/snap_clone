package com.an.core_editor.data.edits

import android.graphics.Bitmap

class CropImageBitmapEdit(
    private val left: Float,
    private val top: Float,
    private val width: Float,
    private val height: Float
): BitmapEdit {
    override fun apply(input: Bitmap): Bitmap {
        val l = left.toInt().coerceIn(0, input.width)
        val t = top.toInt().coerceIn(0, input.height)
        val w = width.toInt().coerceAtMost(input.width - l)
        val h = height.toInt().coerceAtMost(input.height - t)

        return Bitmap.createBitmap(input, l, t, w, h)
    }
}