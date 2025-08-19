package com.an.feature_image_editing.domain.use_cases

import android.graphics.Bitmap
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result
import com.an.feature_image_editing.domain.SubjectDetector
import kotlinx.coroutines.delay

class RemoveBackground(
    private val subjectDetector: SubjectDetector,
    private val bitmapCache: BitmapCache
) {

    suspend operator fun invoke(
        imagePath: String
    ): Result<Unit> {
        val operatedBitmap = bitmapCache.getEdited(imagePath)
            ?: return Result.Failure("Something went wrong")

        var result: Result<Bitmap>? = null
        subjectDetector.detectSubject(
            bitmap = operatedBitmap,
            onSubjectDetected = { bitmap ->

                result = Result.Success(bitmap)
            },
            onError = { message ->
                result = Result.Failure(message)
            }
        )
        while(result == null) {
            delay(100)
        }

        val res = (result as Result<Bitmap>)
        if(res is Result.Success) {
            bitmapCache.updateEdited(imagePath, res.data)
            return Result.Success(Unit)
        } else {
            return Result.Failure("Something went wrong")
        }

    }

}