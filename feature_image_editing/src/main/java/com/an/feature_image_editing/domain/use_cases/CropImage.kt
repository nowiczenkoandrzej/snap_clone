package com.an.feature_image_editing.domain.use_cases

import android.graphics.RectF
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Result
import com.an.feature_image_editing.presentation.util.cropToRect

class CropImage(
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {


    suspend operator fun invoke(
        srcRect: Rect,
    ): Result<Unit> {
        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")


        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = editedElement.copy(
                viewRect = srcRect,
                version = System.currentTimeMillis()
            ),
            saveUndo = true
        )
        return Result.Success(Unit)
    }

}