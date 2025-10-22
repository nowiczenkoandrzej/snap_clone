package com.an.feature_stickers.domain.use_cases

import android.graphics.Bitmap
import com.an.core_editor.data.BitmapCache
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


        val paths = editedImage.cutPaths + cuttingPath

        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = editedImage.copy(
                cutPaths = paths,
                version = System.currentTimeMillis()
            ),
            saveUndo = true
        )

        return Result.Success(Unit)

    }

}