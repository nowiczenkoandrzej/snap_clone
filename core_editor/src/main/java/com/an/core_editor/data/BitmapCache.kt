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



}