package com.an.facefilters.canvas.domain

import android.graphics.Bitmap

interface StickerManager {
    fun getCategories(): List<String>
    fun loadStickers(category: StickerCategory): List<String>
    fun saveNewSticker(sticker: Bitmap)
    fun deleteSticker(fileName: String)
    fun loadPngAsBitmap(fileName: String): Bitmap
}

enum class StickerCategory {
    ACTIVITIES, ANIMALS, CLOTHING, EMOJIS, FOOD, MUSIC, OBJECTS
}