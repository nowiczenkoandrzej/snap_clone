package com.an.feature_drawing.presentation.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawThicknessCurve(
    thickness: Float,
    size: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(
            x = size,
            y = 0f
        )
        quadraticTo(
            x1 = size / 3,
            y1 = size / 3,
            x2 = size / 2,
            y2 = size / 2
        )
        quadraticTo(
            x1 = size / 3 * 2,
            y1 = size / 3 * 2,
            x2 = 0f,
            y2 = size
        )

    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}