package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Result
import com.an.feature_image_editing.presentation.util.PathData
import com.an.feature_image_editing.presentation.util.drawPaths

class SaveDrawings(
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        paths: List<PathData>,
    ): Result<Unit> {

        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Something went wrong")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val editedBitmap = bitmapCache.getEdited(editedElement.image.path)
            ?: return Result.Failure("Something went wrong")

        val updatedElement = editorRepository.getSelectedElement()

        val newBitmap = editedBitmap.drawPaths(
            paths = paths
        )

        bitmapCache.updateEdited(
            path = editedElement.image.path,
            newBitmap = newBitmap
        )


        val newElement = (updatedElement as DomainImageModel).copy()
        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = newElement,
            saveUndo = true
        )
        return Result.Success(Unit)

    }
}