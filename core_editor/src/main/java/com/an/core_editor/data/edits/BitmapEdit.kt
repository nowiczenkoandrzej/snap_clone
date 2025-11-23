package com.an.core_editor.data.edits

import android.graphics.Bitmap

interface BitmapEdit {
    fun apply(input: Bitmap): Bitmap
}