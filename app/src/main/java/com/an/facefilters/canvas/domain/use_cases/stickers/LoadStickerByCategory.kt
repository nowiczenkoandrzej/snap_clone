package com.an.facefilters.canvas.domain.use_cases.stickers

import com.an.facefilters.canvas.domain.Result
import com.an.facefilters.canvas.domain.StickerCategory
import com.an.facefilters.canvas.domain.StickerManager

class LoadStickerByCategory(
    private val stickerManager: StickerManager
) {

    operator fun invoke(
        category: StickerCategory
    ): Result<List<String>> {
        return try {
             Result.Success(stickerManager.loadStickers(category))
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
    }

}