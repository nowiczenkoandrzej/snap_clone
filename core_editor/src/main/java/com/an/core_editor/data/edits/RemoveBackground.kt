package com.an.core_editor.data.edits

import android.graphics.Bitmap

class RemoveBackground(val mask: BooleanArray): ImageEdit {
    override fun apply(input: Bitmap): Bitmap {
        val w = input.width
        val h = input.height
        if (mask.size != w * h) return input

        val pixels = IntArray(w * h)
        input.getPixels(pixels, 0, w, 0, 0, w, h)
        for (i in pixels.indices) {
            if (!mask[i]) {
                pixels[i] = pixels[i] and 0x00FFFFFF
            }
        }
        input.setPixels(pixels, 0, w, 0, 0, w, h)


        return input

    }

}
