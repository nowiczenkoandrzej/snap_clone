package com.an.feature_stickers.domain

import android.graphics.Bitmap
import com.an.core_editor.domain.model.Point

interface StickerManager {
    suspend fun loadCategories(): List<String>
    suspend fun loadStickersByCategory(category: String): List<String>
    suspend fun getSticker(stickerPath: String)
    suspend fun loadStickersMap(): Map<String, List<String>>
}
