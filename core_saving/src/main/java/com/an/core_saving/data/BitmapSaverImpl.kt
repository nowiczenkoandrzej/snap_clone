package com.an.core_saving.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.an.core_saving.domain.BitmapSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class BitmapSaverImpl(
    private val context: Context
): BitmapSaver {

    private val imagesDir: File by lazy {
        File(context.filesDir, "images").apply {
            mkdirs()
        }
    }

    override suspend fun save(bitmap: Bitmap): String {

        return withContext(Dispatchers.IO) {
            val file = File(imagesDir, "img_${UUID.randomUUID()}.png")

            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            "images/${file.name}"

        }


    }

    override suspend fun load(path: String): Bitmap {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, path)
            require(file.exists()) { "Bitmap file not found: $path" }
            BitmapFactory.decodeFile(path)
                ?: error("Failed to decode bitmap: $path")
        }
    }


}