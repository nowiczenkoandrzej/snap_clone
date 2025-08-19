package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result

class ChangeElementAlpha(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        newAlpha: Float
    ): Result<Unit> {

        if(newAlpha < 0f || newAlpha > 1f)
            return Result.Failure("Something went wrong")

        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        val newElement = editedElement.setAlpha(newAlpha)

        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = newElement,
            saveUndo = true
        )

        return Result.Success(Unit)

    }

}