package com.an.feature_text.domain

import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Result

class AddText(
    private val editorRepository: EditorRepository
) {
    suspend operator fun invoke(
        text: String,
        fontSize: Float,
        fontColor: DomainColor,
        fontFamily: DomainFontFamily
    ): Result<Unit> {

        if(text.isEmpty()) return Result.Failure("Empty text!")

        if(fontSize < 0) return Result.Failure("Something went wrong...")


        val textModel = DomainTextModel(
            rotationAngle = 0f,
            scale = 1f,
            position = Point.ZERO,
            alpha = 1f,
            text = text,
            fontSize = fontSize,
            fontColor = fontColor,
            fontFamily = fontFamily
        )

        editorRepository.addElement(textModel)

        return Result.Success(Unit)
    }
}