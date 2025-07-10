package com.an.facefilters.canvas.domain.use_cases.stickers

import android.graphics.Bitmap
import com.an.facefilters.canvas.data.DetectionException
import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.domain.StickerManager

class CreateNewSticker(
    private val subjectDetector: SubjectDetector,
    private val stickerManager: StickerManager
) {
    operator fun invoke(
        bitmap: Bitmap,
        onStickerCreated: (Bitmap) -> Unit
    ) {
        subjectDetector.detectSubject(
            bitmap = bitmap,
            onSubjectDetected = { newSticker ->
                stickerManager.saveNewSticker(sticker = newSticker)
                onStickerCreated(newSticker)
            },
            onError = { error ->
                throw DetectionException(error)
            }
        )
    }
}