package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor
import kotlinx.coroutines.flow.first

class CropImage(
    private val projectEditor: ProjectEditor
) {


    suspend operator fun invoke(
        left: Float,
        top: Float,
        width: Float,
        height: Float
    ): Result<Unit> {
        val editedElement = projectEditor.selectedImage.first()
            ?: return Result.Failure("Couldn't find element")

        val newEditList = editedElement.edits + DomainImageEdit.CropImage(
            left = left,
            top = top,
            width = width,
            height = height
        )

        projectEditor.updateElement(
            newElement = editedElement.copy(
                edits = newEditList,
                version = System.currentTimeMillis()
            ),
            saveUndo = true
        )
        return Result.Success(Unit)
    }

}