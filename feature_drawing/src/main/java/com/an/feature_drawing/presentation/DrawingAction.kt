package com.an.feature_drawing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.feature_drawing.presentation.util.DrawingMode

sealed interface DrawingAction {
    data class SetMode(val mode: DrawingMode): DrawingAction
    data class SelectThickness(val thickness: Float): DrawingAction
    data class UpdateCurrentPath(val offset: Offset, val scale: Float): DrawingAction
    data class SelectColor(val color: Color): DrawingAction
    object AddNewPath: DrawingAction
    object SaveDrawings: DrawingAction
    object Cancel: DrawingAction
    object UndoPath: DrawingAction
}