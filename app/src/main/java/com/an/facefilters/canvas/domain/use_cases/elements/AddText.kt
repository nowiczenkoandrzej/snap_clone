package com.an.facefilters.canvas.domain.use_cases.elements

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.TextModel

class AddText {

    operator fun invoke(
        text: String,
        fontFamily: FontFamily,
        elements: List<Element>,
        color: Color
    ): List<Element> {
        return elements + TextModel(
            text = text,
            textStyle = TextStyle(
                fontFamily = fontFamily,
                color = color,
                fontSize = 60.sp
            ),
            p1 = Offset.Zero
        )
    }
}