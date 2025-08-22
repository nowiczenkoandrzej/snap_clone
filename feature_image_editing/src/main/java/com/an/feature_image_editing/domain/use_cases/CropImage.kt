package com.an.feature_image_editing.domain.use_cases

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Result
import com.an.feature_image_editing.presentation.util.cropToRect

class CropImage(
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {


    suspend operator fun invoke(
        srcRect: Rect,
        viewSize: IntSize,
    ): Result<Unit> {
        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val imagePath = editedElement.image.path

        val editedBitmap = bitmapCache.getEdited(imagePath)
            ?: return Result.Failure("Couldn't find element")

        val originalBitmap = bitmapCache.getOriginal(imagePath)
            ?: return Result.Failure("Couldn't find element")

        val croppedEditedBitmap = editedBitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        val croppedOriginalBitmap = originalBitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        bitmapCache.updateEdited(
            path = imagePath,
            newBitmap = croppedEditedBitmap
        )
        bitmapCache.updateOriginal(
            path = imagePath,
            newBitmap = croppedOriginalBitmap
        )


        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = editedElement.copy(
                image = editedElement.image.copy(
                    width = croppedEditedBitmap.width,
                    height = croppedEditedBitmap.height
                )
            ),
            saveUndo = true
        )
        return Result.Success(Unit)
    }

}