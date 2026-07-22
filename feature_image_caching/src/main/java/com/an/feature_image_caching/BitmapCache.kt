package com.an.feature_image_caching

import android.graphics.Bitmap

class BitmapCache(
    private val bitmapSaver: BitmapSaver
) {

    private val cachedImages: MutableMap<String, Bitmap> = mutableMapOf()
    private val cachedEdited: MutableMap<String, Bitmap> = mutableMapOf()


    suspend fun getOriginalBitmap(path: String): Bitmap? {

        if(cachedImages[path]  == null) {

            val bitmapFromFile = bitmapSaver.loadBitmap(path) ?: return null

            cachedImages.put(
                key = path,
                value = bitmapFromFile
            )
        }

        return cachedImages[path]

    }

    suspend fun saveAndAddToCache(
        bitmap: Bitmap
    ): String {
        val path = bitmapSaver.saveBitmap(bitmap)
        cachedImages.put(
            key = path,
            value = bitmap
        )
        return path
    }

    fun removeFromCache(path: String) {
        cachedImages.remove(path)
    }

    fun setEditedBitmap(path: String, editedBitmap: Bitmap) {
        cachedEdited.put(path, editedBitmap)
    }

    fun getEditedBitmap(path: String): Bitmap? {
        return cachedEdited[path]
    }


}