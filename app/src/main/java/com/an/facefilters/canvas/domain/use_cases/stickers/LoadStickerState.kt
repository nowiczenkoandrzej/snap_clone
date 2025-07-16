package com.an.facefilters.canvas.domain.use_cases.stickers

import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.managers.StickerCategory
import com.an.facefilters.canvas.domain.managers.StickerManager
import com.an.facefilters.canvas.presentation.StickersState

class LoadStickerState (
    private val stickerManager: StickerManager
) {
    suspend operator fun invoke(): Result<StickersState> {
        return try {
            Result.Success(
                StickersState(
                categories = stickerManager.loadCategories(),
                stickers = stickerManager.loadStickersByCategory(StickerCategory.ACTIVITIES),
                userStickers = stickerManager.loadUserStickers()
            )
            )
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
    }
}