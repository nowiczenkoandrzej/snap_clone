package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Result

class CropImage(
    private val editorRepository: EditorRepository
) {


    suspend operator fun invoke(
        left: Float,
        top: Float,
        width: Float,
        height: Float
    ): Result<Unit> {
        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val newEditList = editedElement.edits + DomainImageEdit.CropImage(
            left = left,
            top = top,
            width = width,
            height = height
        )

        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = editedElement.copy(
                edits = newEditList,
                version = System.currentTimeMillis()
            ),
            saveUndo = true
        )
        return Result.Success(Unit)
    }

}