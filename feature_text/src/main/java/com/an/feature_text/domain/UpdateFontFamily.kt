package com.an.feature_text.domain

import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.Result

class UpdateFontFamily(
    private val editorRepository: EditorRepository
) {
    suspend operator fun invoke(
        fontFamily: DomainFontFamily,
        saveUndo: Boolean = true
    ): Result<Unit> {
        val state = editorRepository.state.value

        if(state.selectedElementIndex == null) return Result.Failure("Element not Found")

        if(state.selectedElementIndex!! >= state.elements.size) return Result.Failure("Element not Found")
        if(state.selectedElementIndex!! < 0) return Result.Failure("Element not Found")

        val element = state.elements[state.selectedElementIndex!!]

        if(element !is DomainTextModel) return Result.Failure("Element not Found")

        editorRepository.updateElement(
            index = state.selectedElementIndex!!,
            newElement = element.copy(
                fontFamily = fontFamily
            ),
            saveUndo = saveUndo
        )
        return Result.Success(Unit)
    }
}