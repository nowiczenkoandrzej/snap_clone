package com.an.facefilters.canvas.domain.use_cases.stickers

import com.an.facefilters.canvas.domain.Result
import com.an.facefilters.canvas.domain.StickerCategory
import com.an.facefilters.canvas.domain.StickerManager
import com.an.facefilters.canvas.domain.StickersState

class LoadStickerState (
    private val stickerManager: StickerManager
) {
    operator fun invoke(): Result<StickersState> {
        return try {
            Result.Success(StickersState(
                categories = stickerManager.loadCategories(),
                stickers = stickerManager.loadStickersByCategory(StickerCategory.ACTIVITIES),
                userStickers = stickerManager.loadUserStickers()
            ))
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
    }
}