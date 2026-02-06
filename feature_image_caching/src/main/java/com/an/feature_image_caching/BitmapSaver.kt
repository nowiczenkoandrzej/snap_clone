package com.an.feature_image_caching

import android.graphics.Bitmap

interface BitmapSaver {
    suspend fun saveBitmap(bitmap: Bitmap): String
    suspend fun loadBitmap(path: String): Bitmap?
}