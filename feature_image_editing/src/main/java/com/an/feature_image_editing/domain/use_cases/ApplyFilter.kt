package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor
import com.an.feature_image_editing.presentation.util.PhotoFilter
import kotlinx.coroutines.flow.first

class ApplyFilter(
    private val projectEditor: ProjectEditor
) {

    suspend operator fun invoke(
        filter: PhotoFilter,
    ): Result<Unit> {

        val editedElement = projectEditor.selectedImage.first()
            ?: return Result.Failure("Couldn't find element")

        val newEditList = editedElement.edits + DomainImageEdit.ApplyFilter(filter.name)

        val newElement = editedElement.copy(
            edits = newEditList,
            currentFilter = filter.name,
            version = System.currentTimeMillis()
        )


        projectEditor.updateElement(
            newElement = newElement,
            saveUndo = true,
        )

        return Result.Success(Unit)

    }

}