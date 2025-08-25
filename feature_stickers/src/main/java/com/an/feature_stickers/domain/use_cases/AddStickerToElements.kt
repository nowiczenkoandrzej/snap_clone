package com.an.feature_stickers.domain.use_cases

import android.content.Context
import android.graphics.BitmapFactory
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.ImageData
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Result
import com.an.feature_stickers.domain.StickerManager

class AddStickerToElements(
    private val editorRepository: EditorRepository,
    private val context: Context
) {

    suspend operator fun invoke(
        stickerPath: String
    ): Result<Unit> {

        editorRepository.addElement(DomainStickerModel(
            rotationAngle = 0f,
            scale = 1f,
            position = Point.ZERO,
            alpha = 1f,
            stickerPath = stickerPath
        ))

        return Result.Success(Unit)

    }


}