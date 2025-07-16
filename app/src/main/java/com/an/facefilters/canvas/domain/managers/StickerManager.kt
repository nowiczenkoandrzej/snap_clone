package com.an.facefilters.canvas.domain.managers

import android.graphics.Bitmap
import java.io.File

interface StickerManager {
    suspend fun loadCategories(): List<String>
    suspend fun loadStickersByCategory(category: StickerCategory): List<String>
    fun saveNewSticker(sticker: Bitmap)
    suspend fun deleteSticker(file: File)
    suspend fun loadPngAsBitmap(fileName: String): Bitmap
    suspend fun loadUserStickers(): List<File>
}

enum class StickerCategory {
    ACTIVITIES, ANIMALS, CLOTHING, EMOJIS, FOOD, MUSIC, OBJECTS, YOURS
}