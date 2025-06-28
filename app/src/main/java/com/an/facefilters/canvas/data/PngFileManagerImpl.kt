package com.an.facefilters.canvas.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.an.facefilters.canvas.domain.PngFileManager
import java.io.File
import java.util.UUID

class PngFileManagerImpl(
    private val context: Context
): PngFileManager {

    companion object {
        const val USER_STICKER = "user_sticker"
    }

    override fun saveSticker(bitmap: Bitmap) {
        val dir = File(context.filesDir, USER_STICKER)
        if(!dir.exists()) dir.mkdirs()

        val filename = "${UUID.randomUUID()}.png"
        val file = File(dir, filename)

        file.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }


    }

    override fun loadUserStickers(): List<File> {
        val dir = File(context.filesDir, USER_STICKER)
        return dir.listFiles()?.filter { it.extension == "png" } ?: emptyList()
    }

}

fun File.toBitmap(): Bitmap{
    return BitmapFactory.decodeFile(this.path)
}