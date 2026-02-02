package com.an.core_saving.domain

import android.graphics.Bitmap

interface BitmapSaver {
    suspend fun saveBitmap(bitmap: Bitmap): String
    suspend fun loadBitmap(path: String): Bitmap?
}