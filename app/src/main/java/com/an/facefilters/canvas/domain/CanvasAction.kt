package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.ToolType

sealed interface CanvasAction {

    data class TransformLayer(
        val scale: Float,
        val rotation: Float,
        val offset: Offset
    ): CanvasAction

    data class DragAndDropLayers(
        val fromIndex: Int,
        val toIndex: Int
    ): CanvasAction

    data class AddImage(val bitmap: Bitmap): CanvasAction

    data class SelectTool(val tool: ToolType): CanvasAction

    data class SelectLayer(val index: Int): CanvasAction

    data class ChangeSliderPosition(val position: Float): CanvasAction

    data class SetMode(val mode: Mode): CanvasAction

    object StartDrawingPath: CanvasAction
    data class DrawPath(val offset: Offset): CanvasAction
    object EndDrawingPath: CanvasAction

    object SelectLayersMode: CanvasAction

    object ShowToolsSelector: CanvasAction
    object HideToolsSelector: CanvasAction
    object ConsumeEvent: CanvasAction

}