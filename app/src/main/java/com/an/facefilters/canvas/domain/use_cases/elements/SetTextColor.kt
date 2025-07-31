package com.an.facefilters.canvas.domain.use_cases.elements

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.model.TextModel

class SetTextColor {

    operator fun invoke(
        element: Element?,
        color: Color
    ): Result<TextModel> {
        if(element == null) return Result.Failure("Pick Text")
        if(element is TextModel) {
            val newElement = element.copy(
                textStyle = element.textStyle.copy(
                    color = color
                )
            )
            return Result.Success(newElement)
        } else {
            return Result.Failure("Pick Text")
        }
    }
}