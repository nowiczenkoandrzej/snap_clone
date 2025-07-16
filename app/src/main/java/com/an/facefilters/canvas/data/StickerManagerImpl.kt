package com.an.facefilters.canvas.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.an.facefilters.canvas.domain.managers.PngFileManager
import com.an.facefilters.canvas.domain.managers.StickerCategory
import com.an.facefilters.canvas.domain.managers.StickerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class StickerManagerImpl(
    private val context: Context,
    private val fileManager: PngFileManager
): StickerManager {

    private val basePath = "stickers"

    override suspend fun loadCategories(): List<String> {

        val userSticker = fileManager.loadUserStickers()

        val categories = mutableListOf<String>()

        if(userSticker.isNotEmpty()) categories.add("yours")

        val stickersDirs = context.assets.list(basePath)?.toList().orEmpty()

        categories.addAll(stickersDirs)

        return categories.toList()
    }

    override suspend fun loadStickersByCategory(category: StickerCategory): List<String> {
        val path = "$basePath/${category.name.lowercase()}"
        return context.assets
            .list(path)
            ?.toList()
            .orEmpty()
    }

    override suspend fun loadPngAsBitmap(fileName: String): Bitmap {
        val inputStream = context.assets.open(fileName)
        return BitmapFactory.decodeStream(inputStream)
    }

    override suspend fun loadUserStickers(): List<File> {
        return fileManager.loadUserStickers()
    }

    override fun saveNewSticker(sticker: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            fileManager.saveSticker(sticker)
        }
    }

    override suspend fun deleteSticker(file: File) {
        if(file.exists())
            file.delete()
    }
}