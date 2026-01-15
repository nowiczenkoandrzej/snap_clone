package com.an.feature_drawing.presentation.util

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color

sealed interface DrawingMode {

    val defaultColor: Color
    val blendMode: BlendMode?

    object Pencil: DrawingMode {
        override val defaultColor = Color.Black
        override val blendMode = null
    }

    object Eraser: DrawingMode {
        override val defaultColor = Color.Transparent
        override val blendMode = BlendMode.Clear
    }

    object Cut: DrawingMode {
        override val defaultColor = Color.Black
        override val blendMode = null
    }

}

enum class DrawingModeArg {
    PENCIL,
    ERASER,
    CUT
}

fun DrawingModeArg.toDrawingMode(): DrawingMode =
    when (this) {
        DrawingModeArg.PENCIL -> DrawingMode.Pencil
        DrawingModeArg.ERASER -> DrawingMode.Eraser
        DrawingModeArg.CUT -> DrawingMode.Cut
    }