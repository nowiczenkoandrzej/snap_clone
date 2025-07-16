package com.an.facefilters.canvas.domain.use_cases.stickers

import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.managers.StickerCategory
import com.an.facefilters.canvas.domain.managers.StickerManager

class LoadStickerByCategory(
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