package com.an.feature_drawing.domain.use_cases

import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Result
import com.an.feature_drawing.presentation.DrawingMode

class SaveDrawings(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        paths: List<PathData>,
        mode: DrawingMode
    ): Result<Unit> {

        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val newEdit = when(mode) {
            DrawingMode.Cut -> DomainImageEdit.CutImage(paths.first())
            DrawingMode.Eraser -> DomainImageEdit.DrawRubber(paths)
            DrawingMode.Pencil -> DomainImageEdit.DrawPaths(paths)
        }

        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = editedElement.copy(
                edits = editedElement.edits + newEdit,
                version = System.currentTimeMillis()
            ),
            saveUndo = true
        )

        return Result.Success(Unit)


    }

}