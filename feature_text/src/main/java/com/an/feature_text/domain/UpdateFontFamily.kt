package com.an.feature_text.domain

import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor

class UpdateFontFamily(
    private val projectEditor: ProjectEditor
) {
    suspend operator fun invoke(
        fontFamily: DomainFontFamily,
        saveUndo: Boolean = true
    ): Result<Unit> {
        val editedElement = projectEditor.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")


        if(editedElement !is DomainTextModel) return Result.Failure("Couldn't find element")

        projectEditor.updateElement(
            newElement = editedElement.copy(
                fontFamily = fontFamily
            ),
            saveUndo = saveUndo
        )
        return Result.Success(Unit)
    }
}