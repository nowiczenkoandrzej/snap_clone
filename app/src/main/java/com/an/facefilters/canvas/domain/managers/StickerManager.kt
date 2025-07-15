package com.an.facefilters.canvas.domain.managers

import android.graphics.Bitmap
import java.io.File

interface StickerManager {
    fun loadCategories(): List<String>
    fun loadStickersByCategory(category: StickerCategory): List<String>
    fun saveNewSticker(sticker: Bitmap)
    fun deleteSticker(file: File)
    fun loadPngAsBitmap(fileName: String): Bitmap
    fun loadUserStickers(): List<File>
}

enum class StickerCategory {
    ACTIVITIES, ANIMALS, CLOTHING, EMOJIS, FOOD, MUSIC, OBJECTS, YOURS
}