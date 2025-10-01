package com.an.feature_stickers.domain.use_cases

import android.graphics.Bitmap
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Result

class SaveCutting(
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {
    suspend operator fun invoke(
        editedImage: DomainImageModel,
        bitmap: Bitmap
    ): Result<Unit> {
        val operatedBitmap = bitmapCache.getEdited(editedImage.id)
            ?: return Result.Failure("Something went wrong 11")

        val newBitmapId = bitmapCache.updateEdited(
            id = editedImage.id,
            newBitmap = bitmap
        ) ?: return Result.Failure("Something went wrong")
        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = editedImage.copy(
                version = System.currentTimeMillis(),
                id = newBitmapId
            ),
            saveUndo = true
        )

        return Result.Success(Unit)

    }

}