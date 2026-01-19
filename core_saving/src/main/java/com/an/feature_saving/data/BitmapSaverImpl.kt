package com.an.feature_saving.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.an.feature_saving.domain.BitmapSaver
import java.io.File

class BitmapSaverImpl(
    private val context: Context
): BitmapSaver {
    override suspend fun save(bitmap: Bitmap): String {
        val file = File(
            context.filesDir,
            "img_${System.currentTimeMillis()}.png"
        )

        /*file.outputStream().use { out ->
            bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                out
            )
        }*/


        return file.absolutePath
    }

    override suspend fun load(path: String): Bitmap? {
        val file = File(path)

        if(!file.exists()) return null

        return BitmapFactory.decodeFile(file.absolutePath)

    }
}