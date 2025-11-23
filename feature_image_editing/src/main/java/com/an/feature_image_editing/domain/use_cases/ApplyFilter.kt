package com.an.feature_image_editing.domain.use_cases

import android.util.Log
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.data.edits.ImageEdit
import com.an.core_editor.domain.DomainImageEdit
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


        val updatedElement = editorRepository.getSelectedElement()

        val newEditList = editedElement.edits + DomainImageEdit.ApplyFilter(filter.name)

        val newElement = (updatedElement as DomainImageModel).copy(
            edits = newEditList,
            currentFilter = filter.name,
            version = System.currentTimeMillis()
        )


        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = newElement,
            saveUndo = true,
        )

        return Result.Success(Unit)

    }

}