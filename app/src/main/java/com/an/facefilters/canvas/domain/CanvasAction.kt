package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.ToolType

sealed interface CanvasAction

sealed interface UiAction: CanvasAction{

    object ShowToolsSelector: UiAction
    object HideToolsSelector: UiAction
    object ShowColorPicker: UiAction
    object HideColorPicker: UiAction
    object ShowTextInput: UiAction
    object HideTextInput: UiAction
    object ConsumeEvent: UiAction

}

sealed interface DrawingAction: CanvasAction {
    object StartDrawingPath: DrawingAction
    data class DrawPath(val offset: Offset): DrawingAction
    object EndDrawingPath: DrawingAction
    data class SelectThickness(val thickness: Float): DrawingAction
}

sealed interface LayerAction: CanvasAction {
    object TransformStart: LayerAction

    data class TransformLayer(
        val scale: Float,
        val rotation: Float,
        val offset: Offset
    ): LayerAction

    data class DragAndDropLayers(
        val fromIndex: Int,
        val toIndex: Int
    ): LayerAction

    data class AddImage(val bitmap: Bitmap): LayerAction
    data class CropImage(val bitmap: Bitmap): LayerAction
    data class SelectLayer(val index: Int): LayerAction
    data class ChangeSliderPosition(val position: Float): LayerAction

}

sealed interface ToolAction: CanvasAction {
    data class SelectTool(val tool: ToolType): ToolAction
    data class SetMode(val mode: Mode): ToolAction
    object Undo: ToolAction
    object Redo: ToolAction
    data class SelectColor(val color: Color): ToolAction
    data class AddText(val text: String): ToolAction
}