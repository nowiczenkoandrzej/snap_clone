package com.an.feature_image_editing.presentation

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.presentation.model.UiImageModel

data class DrawingState(
    val pathThickness: Float = 16f,
    val currentPath: PathData = PathData.DEFAULT,
    val paths: List<PathData> = emptyList(),
    val editedImg: UiImageModel? = null,
    val selectedColor: Color = Color.Black,
)

data class RubberState(
    val pathThickness: Float = 48f,
    val currentPath: PathData = PathData.DEFAULT,
    val rubberPaths: List<PathData> = emptyList(),
    val changesStack: List<Bitmap> = emptyList()
)


