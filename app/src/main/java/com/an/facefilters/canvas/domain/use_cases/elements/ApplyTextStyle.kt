package com.an.facefilters.canvas.domain.use_cases.elements

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.TextModel

class ApplyTextStyle {
    operator fun invoke(
        element: Element?,
        fontFamily: FontFamily,
        color: Color
    ): Result<TextModel> {
        if (element !is TextModel) return Result.Failure("Pick Text")

        return Result.Success(
            element.copy(
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    color = color,
                    fontSize = 60.sp,

                )
            )
        )
    }
}