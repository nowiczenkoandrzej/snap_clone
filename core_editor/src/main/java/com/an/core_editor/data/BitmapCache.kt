package com.an.core_editor.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class BitmapCache {
    private val originalBitmaps = mutableMapOf<String, Bitmap>()
    private val editedBitmaps = mutableMapOf<String, Bitmap>()

    fun add(path: String, bitmap: Bitmap) {
        originalBitmaps[path] = bitmap
        editedBitmaps[path] = bitmap
    }
    fun resetEdited(path: String) {
        originalBitmaps[path]?.let {
            editedBitmaps[path] = it.copy(Bitmap.Config.ARGB_8888, true)
        }
    }

    fun getOriginal(path: String): Bitmap? {
        return originalBitmaps[path]
    }

    fun getEdited(path: String): Bitmap? {
        return editedBitmaps[path]
    }
    fun updateEdited(path: String, newBitmap: Bitmap) {
        editedBitmaps[path] = newBitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    fun updateOriginal(path: String, newBitmap: Bitmap) {
        originalBitmaps[path] = newBitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    fun clear(path: String) {
        originalBitmaps.remove(path)
        editedBitmaps.remove(path)
    }

    fun clearAll() {
        originalBitmaps.clear()
        editedBitmaps.clear()
    }
}