package com.an.feature_canvas.domain.use_cases

import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.EditorState
import com.an.core_editor.domain.model.Result

class Undo(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(): Result<Unit> {

        editorRepository.undo()
        return Result.Success(Unit)
    }

}