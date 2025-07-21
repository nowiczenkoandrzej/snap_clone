package com.an.facefilters.canvas.data

import android.graphics.Bitmap

object BitmapCache {
    private val cache = mutableMapOf<String, Bitmap>()

    fun put(id: String, bitmap: Bitmap) {
        cache[id] = bitmap
    }

    fun get(id: String): Bitmap? = cache[id]

    fun remove(id: String) = cache.remove(id)

    fun clear() = cache.clear()
}