package com.an.facefilters.canvas.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.an.facefilters.canvas.domain.StickerCategory
import com.an.facefilters.canvas.domain.StickerManager

class StickerManagerImpl(
    private val context: Context
): StickerManager {

    private val basePath = "stickers"

    override fun getCategories(): List<String> {
        return context.assets.list(basePath)?.toList().orEmpty()
    }

    override fun loadStickers(category: StickerCategory): List<String> {
        val path = "$basePath/${category.name.lowercase()}"
        return context.assets
            .list(path)

            ?.toList()
            .orEmpty()
    }

    override fun loadPngAsBitmap(fileName: String): Bitmap {
        val inputStream = context.assets.open(fileName)
        return BitmapFactory.decodeStream(inputStream)
    }

    override fun saveNewSticker(sticker: Bitmap) {
        TODO("Not yet implemented")
    }

    override fun deleteSticker(fileName: String) {
        TODO("Not yet implemented")
    }
}