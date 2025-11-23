package com.an.core_editor.data.edits

import android.graphics.Bitmap
import android.graphics.Color

class RemoveBackgroundBitmapEdit(
    val mask: BooleanArray
): BitmapEdit {
    override fun apply(input: Bitmap): Bitmap {
        val w = input.width
        val h = input.height
        if (mask.size != w * h) return input

        val pixels = IntArray(w * h)
        input.getPixels(pixels, 0, w, 0, 0, w, h)
        for (i in pixels.indices) {
            if (!mask[i]) {
                pixels[i] = Color.TRANSPARENT
            }
        }
        input.setPixels(pixels, 0, w, 0, 0, w, h)


        return input
    }
}