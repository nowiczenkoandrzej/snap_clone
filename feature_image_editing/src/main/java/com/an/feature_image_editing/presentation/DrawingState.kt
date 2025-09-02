package com.an.feature_image_editing.presentation

import androidx.compose.ui.graphics.Color
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.domain.model.PathData

data class DrawingState(
    val pathThickness: Float = 16f,
    val currentPath: PathData = PathData.DEFAULT,
    val paths: List<PathData> = emptyList(),
    val editedImg: UiImageModel? = null,
    val selectedColor: Color = Color.Black,
)
