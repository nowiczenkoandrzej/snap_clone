package com.an.feature_image_editing.domain.use_cases

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Result
import com.an.core_editor.presentation.toOffset
import com.an.feature_image_editing.presentation.util.PhotoFilter
import com.an.feature_image_editing.presentation.util.cropToRect

class ApplyFilter(
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        filter: PhotoFilter,
    ): Result<Unit> {


        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val originalBitmap = bitmapCache.getOriginal(editedElement.id)
            ?: return Result.Failure("Something went wrong")

        val updatedElement = editorRepository.getSelectedElement()

        var newBitmap = filter.apply(originalBitmap)

        editedElement.cropRect?.let {
             newBitmap = newBitmap.cropToRect(
                srcRect = Rect(
                    topLeft = it.topLeft.toOffset(),
                    bottomRight = it.bottomRight.toOffset()
                    ),
                viewSize = IntSize(
                    width = newBitmap.width,
                    height = newBitmap.height
                )

            )
        }


        val newBitmapId = bitmapCache.updateEdited(
            id = editedElement.id,
            newBitmap = newBitmap
        ) ?: return Result.Failure("Something went wrong")



        val newElement = (updatedElement as DomainImageModel).copy(
            currentFilter = filter.name,
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