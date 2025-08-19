package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Result
import com.an.feature_image_editing.presentation.util.PhotoFilter

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

        val originalBitmap = bitmapCache.getOriginal(editedElement.image.path)
            ?: return Result.Failure("Something went wrong")

        val updatedElement = editorRepository.getSelectedElement()

        if(updatedElement !is DomainImageModel)
            Result.Failure("Couldn't find element")

        val newBitmap = filter.apply(originalBitmap)


        bitmapCache.updateEdited(
            path = editedElement.image.path,
            newBitmap = newBitmap
        )

        val newElement = (updatedElement as DomainImageModel).copy(
            image = updatedElement.image.copy(
                currentFilter = filter.name
            )
        )

        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = newElement,
            saveUndo = true
        )

        return Result.Success(Unit)

    }

}