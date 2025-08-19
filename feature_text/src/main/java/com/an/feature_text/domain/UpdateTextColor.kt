package com.an.feature_text.domain

import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.Result

class UpdateTextColor(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        color: DomainColor,
        saveUndo: Boolean = true
    ): Result<Unit> {

        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")
        val state = editorRepository.state.value

        if(editedElement !is DomainTextModel) return Result.Failure("Couldn't find element")

        editorRepository.updateElement(
            index = state.selectedElementIndex!!,
            newElement = editedElement.copy(
                fontColor = color
            ),
            saveUndo = saveUndo
        )
        return Result.Success(Unit)
    }
}