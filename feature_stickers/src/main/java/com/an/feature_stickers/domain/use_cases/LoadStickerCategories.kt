package com.an.feature_stickers.domain.use_cases

import com.an.core_editor.domain.model.Result
import com.an.feature_stickers.domain.StickerManager

class LoadStickerCategories(
    private val stickerManager: StickerManager
) {

    suspend operator fun invoke(): Result<List<String>> {
        return try {
            Result.Success(stickerManager.loadCategories())
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
    }

}