package com.an.facefilters.canvas.domain.use_cases.stickers

import android.graphics.Bitmap
import com.an.facefilters.canvas.domain.Result
import com.an.facefilters.canvas.domain.StickerManager

class LoadSticker(
    private val stickerManager: StickerManager
) {
    operator fun invoke(
        path: String
    ): Result<Bitmap> {
        return try {
            Result.Success(stickerManager.loadPngAsBitmap(path))
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }

    }

}