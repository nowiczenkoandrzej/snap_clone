package com.an.feature_drawing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.core_editor.domain.model.PathData
import com.an.feature_drawing.presentation.util.DrawingMode

data class DrawingState(
    val mode: DrawingMode = DrawingMode.Pencil,
    val pathThickness: Float = 16f,
    val currentPath: PathData = PathData.DEFAULT,
    val paths: List<PathData> = emptyList(),
    val selectedColor: Color = Color.Black,
    val fingerPosition: Offset? = null,
    val showMagnifier: Boolean = false
)