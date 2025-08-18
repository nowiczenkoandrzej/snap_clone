package com.an.feature_canvas.domain.use_cases

import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result

class DeleteElement(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        index: Int
    ): Result<Unit> {
        val state = editorRepository.state.value

        if(state.elements.size <= index || index < 0) return Result.Failure("Element not Found")

        editorRepository.removeElement(index)

        return Result.Success(Unit)

    }
}