package com.an.feature_image_editing.domain.use_cases

import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Result

class ApplyRubber(
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        paths: List<PathData>,
    ): Result<Unit> {
        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val newEditsList = editedElement.edits + DomainImageEdit.DrawRubber(paths)

        val newElement = editedElement.copy(
            edits = newEditsList,
            version = System.currentTimeMillis()
        )
        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = newElement,
            saveUndo = true
        )
        return Result.Success(Unit)

    }

}