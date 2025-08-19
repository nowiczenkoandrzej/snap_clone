package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result

class DeleteImage(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(): Result<Unit> {

        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        editorRepository.removeElement(editorRepository.state.value.selectedElementIndex!!)

        return Result.Success(Unit)

    }
}