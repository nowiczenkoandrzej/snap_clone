package com.an.feature_drawing.domain.use_cases

import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor
import com.an.feature_drawing.presentation.util.DrawingMode
import kotlinx.coroutines.flow.first

class SaveDrawings(
    private val projectEditor: ProjectEditor,
) {
    suspend operator fun invoke(
        paths: List<PathData>,
        mode: DrawingMode
    ): Result<Unit> {

        val editedElement = projectEditor.selectedImage.first()
            ?: return Result.Failure("Couldn't find element")


        val newEdit = when(mode) {
            DrawingMode.Cut -> DomainImageEdit.CutImage(paths.first())
            DrawingMode.Eraser -> DomainImageEdit.DrawRubber(paths)
            DrawingMode.Pencil -> DomainImageEdit.DrawPaths(paths)
        }

        projectEditor.updateElement(
            newElement = editedElement.copy(
                edits = editedElement.edits + newEdit,
                version = System.currentTimeMillis()
            ),
            saveUndo = true
        )

        return Result.Success(Unit)


    }

}