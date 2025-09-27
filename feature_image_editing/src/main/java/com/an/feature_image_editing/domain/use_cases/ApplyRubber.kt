package com.an.feature_image_editing.domain.use_cases

import android.graphics.Bitmap
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Result
import com.an.feature_image_editing.presentation.util.drawPaths
import com.an.feature_image_editing.presentation.util.drawRubber

class ApplyRubber(
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        newBitmap: Bitmap,
    ): Result<Unit> {
        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")


        val newBitmapId = bitmapCache.updateEdited(
            id = editedElement.id,
            newBitmap = newBitmap
        ) ?: return Result.Failure("Something went wrong")

        val newElement = editedElement.copy(
            id = newBitmapId,
            version = System.currentTimeMillis()
        )
        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = newElement,
            saveUndo = true
        )
        return Result.Success(Unit)

    }

}