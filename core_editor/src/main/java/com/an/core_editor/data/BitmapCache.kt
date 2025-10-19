package com.an.core_editor.data

import android.graphics.Bitmap

class BitmapCache {



    private val srcBitmaps = mutableMapOf<String, Bitmap>()
    private val editedBitmaps = mutableMapOf<String, Bitmap>()

    fun add(path: String, bitmap: Bitmap) {
        srcBitmaps[path] = bitmap
    }

    fun get(path: String): Bitmap? {
        return srcBitmaps[path]
    }

    fun addEdited(id: String, bitmap: Bitmap) {
        editedBitmaps[id] = bitmap
    }

    fun getEdited(id: String): Bitmap? {
        return editedBitmaps[id]
    }

    fun isOnEditedList(id: String): Boolean {
        if(editedBitmaps[id] == null) {
            return false
        } else {
            return true
        }
    }
    /*fun add(id: String, bitmap: Bitmap) {
        originalBitmaps[id] = bitmap
        editedBitmaps[id] = bitmap
        editedToOriginal[id] = id
    }

    fun getOriginal(id: String): Bitmap? {
        val originalId = editedToOriginal[id]
        return originalBitmaps[originalId]
    }


    fun updateEdited(id: String, newBitmap: Bitmap): String? {
        val newId = UUID.randomUUID().toString()
        editedBitmaps[newId] = newBitmap.copy(Bitmap.Config.ARGB_8888, true)

        val originalBitmapId = editedToOriginal[id] ?: return null

        editedToOriginal[newId] = originalBitmapId

        return newId
    }*/

}