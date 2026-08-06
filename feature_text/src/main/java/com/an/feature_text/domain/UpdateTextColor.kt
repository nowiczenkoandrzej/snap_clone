package com.an.feature_text.domain

import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor

class UpdateTextColor(
    private val projectEditor: ProjectEditor
) {

    suspend operator fun invoke(
        color: DomainColor,
        saveUndo: Boolean = true
    ): Result<Unit> {

        val editedElement = projectEditor.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainTextModel) return Result.Failure("Couldn't find element")

        projectEditor.updateElement(
            newElement = editedElement.copy(
                fontColor = color
            ),
            saveUndo = saveUndo
        )
        return Result.Success(Unit)
    }
}