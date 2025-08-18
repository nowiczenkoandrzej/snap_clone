package com.an.feature_canvas.domain.use_cases

import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result

class ReorderElements(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        from: Int,
        to: Int
    ): Result<Unit> {

        val state = editorRepository.state.value

        val size = state.elements.size

        if(from >= size || from < 0 || to >= size || to < 0) return Result.Failure("Element not Found")

        editorRepository.reorderElements(
            fromIndex = from,
            toIndex = to
        )

        return Result.Success(Unit)
    }

}