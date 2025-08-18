package com.an.feature_canvas.domain.use_cases

import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result

class SelectElement(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        index: Int
    ): Result<Unit> {
        val state = editorRepository.state.value

        if(state.elements.size <= index || index < 0) return Result.Failure("Element not Found")

        editorRepository.selectElement(index)

        return Result.Success(Unit)
    }
}