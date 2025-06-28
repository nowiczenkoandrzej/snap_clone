package com.an.facefilters.canvas.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.an.facefilters.canvas.domain.PngFileManager
import com.an.facefilters.canvas.domain.StickerCategory
import com.an.facefilters.canvas.domain.StickerManager
import java.io.File

class StickerManagerImpl(
    private val context: Context,
    private val fileManager: PngFileManager
): StickerManager {

    private val basePath = "stickers"

    override fun getCategories(): List<String> {

        val userSticker = fileManager.loadUserStickers()

        val categories = mutableListOf<String>()

        if(userSticker.isNotEmpty()) categories.add("yours")

        val stickersDirs = context.assets.list(basePath)?.toList().orEmpty()

        categories.addAll(stickersDirs)

        return categories.toList()
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

    override fun loadUserStickers(): List<File> {
        return fileManager.loadUserStickers()
    }

    override fun saveNewSticker(sticker: Bitmap) {
        fileManager.saveSticker(sticker)
    }

    override fun deleteSticker(file: File) {
        if(file.exists())
            file.delete()
    }
}