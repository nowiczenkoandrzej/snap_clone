package com.an.feature_stickers.domain.use_cases

import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor

class AddStickerToElements(
    private val projectEditor: ProjectEditor
) {

    suspend operator fun invoke(
        stickerPath: String,
    ): Result<Unit> {

        projectEditor.addElement(DomainStickerModel(
            rotationAngle = 0f,
            scale = 1f,
            position = Point.ZERO,
            alpha = 1f,
            stickerPath = stickerPath,
        ))

        return Result.Success(Unit)

    }


}