package com.an.facefilters.canvas.domain.use_cases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.TextModel

class SelectFontFamily {

    operator fun invoke(
        element: Element,
        fontFamily: FontFamily,
        color: Color
    ):TextModel {
        if(element is TextModel) {
            val newElement = element.copy(
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    color = color,
                    fontSize = 60.sp
                )
            )
            return newElement
        } else {
            throw Exception("Select Text")
        }
    }
}