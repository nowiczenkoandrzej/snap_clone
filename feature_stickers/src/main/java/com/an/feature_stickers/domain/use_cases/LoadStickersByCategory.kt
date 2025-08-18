package com.an.feature_stickers.domain.use_cases

import com.an.core_editor.domain.model.Result
import com.an.feature_stickers.domain.StickerCategory
import com.an.feature_stickers.domain.StickerManager

class LoadStickersByCategory(
    private val stickerManager: StickerManager
) {

    suspend operator fun invoke(
        category: StickerCategory
    ): Result<List<String>> {
        return try {
            Result.Success(stickerManager.loadStickersByCategory(category))
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
    }

}