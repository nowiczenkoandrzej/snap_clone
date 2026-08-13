package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor
import kotlinx.coroutines.flow.first

class ChangeElementAlpha(
    private val projectEditor: ProjectEditor
) {

    suspend operator fun invoke(
        newAlpha: Float
    ): Result<Unit> {

        if(newAlpha < 0f || newAlpha > 1f)
            return Result.Failure("Something went wrong")

        val editedElement = projectEditor.selectedImage.first()
            ?: return Result.Failure("Couldn't find element")

        val newElement = editedElement.setAlpha(newAlpha)

        projectEditor.updateElement(
            newElement = newElement,
            saveUndo = true
        )

        return Result.Success(Unit)

    }

}