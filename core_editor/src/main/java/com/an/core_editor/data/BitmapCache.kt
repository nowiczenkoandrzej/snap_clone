package com.an.core_editor.data

import android.graphics.Bitmap
import java.util.UUID

class BitmapCache {
    private val originalBitmaps = mutableMapOf<String, Bitmap>()
    private val editedBitmaps = mutableMapOf<String, Bitmap>()
    private val editedToOriginal = mutableMapOf<String, String>()


    fun add(id: String, bitmap: Bitmap) {
        originalBitmaps[id] = bitmap
        editedBitmaps[id] = bitmap
        editedToOriginal[id] = id
    }
    fun resetEdited(id: String) {
        originalBitmaps[id]?.let {
            editedBitmaps[id] = it.copy(Bitmap.Config.ARGB_8888, true)
        }
    }

    fun getOriginal(id: String): Bitmap? {
        val originalId = editedToOriginal[id]
        return originalBitmaps[originalId]
    }

    fun getEdited(id: String): Bitmap? {
        return editedBitmaps[id]
    }
    fun updateEdited(id: String, newBitmap: Bitmap): String? {
        val newId = UUID.randomUUID().toString()
        editedBitmaps[newId] = newBitmap.copy(Bitmap.Config.ARGB_8888, true)

        val originalBitmapId = editedToOriginal[id] ?: return null

        editedToOriginal[newId] = originalBitmapId

        return newId
    }

    fun clearUnused(usedIds: Set<String>) {
        val toRemove = editedBitmaps.keys - usedIds
        toRemove.forEach {
            editedBitmaps.remove(it)
            editedToOriginal.remove(it)
        }
    }

    fun updateOriginal(id: String, newBitmap: Bitmap) {
        originalBitmaps[id] = newBitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    fun clear(id: String) {
        originalBitmaps.remove(id)
        editedBitmaps.remove(id)
    }

    fun clearAll() {
        originalBitmaps.clear()
        editedBitmaps.clear()
    }
}