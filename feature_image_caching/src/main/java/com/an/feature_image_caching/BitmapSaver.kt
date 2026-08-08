package com.an.feature_image_caching

import android.graphics.Bitmap

interface BitmapSaver {
    suspend fun saveBitmap(
        bitmap: Bitmap,
        qualityPercentage: Int = 100
    ): String
    suspend fun loadBitmap(path: String): Bitmap?
}