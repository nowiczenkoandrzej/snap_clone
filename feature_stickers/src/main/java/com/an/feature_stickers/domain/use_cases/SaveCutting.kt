package com.an.feature_stickers.domain.use_cases

import android.graphics.Bitmap
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Result

class SaveCutting(
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {
    suspend operator fun invoke(
        editedImage: DomainImageModel,
        cuttingPath: PathData
    ): Result<Unit> {


        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val newEditList = editedElement.edits + DomainImageEdit.CutImage(cuttingPath)


        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = editedElement.copy(
                edits = newEditList,
                version = System.currentTimeMillis()
            ),
            saveUndo = true
        )

        return Result.Success(Unit)

    }

}