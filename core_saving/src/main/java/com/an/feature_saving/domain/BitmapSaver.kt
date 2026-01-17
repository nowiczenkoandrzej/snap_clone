package com.an.feature_saving.domain

import android.graphics.Bitmap

interface BitmapSaver {

    suspend fun save(bitmap: Bitmap): String
    suspend fun load(path: String): Bitmap?
}