package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import com.an.facefilters.canvas.domain.model.Tools

sealed interface CanvasAction {

    data class TransformLayer(
        val scale: Float,
        val rotation: Float,
        val offset: Offset
    ): CanvasAction

    data class AddImage(
        val bitmap: Bitmap
    ): CanvasAction

    data class SelectTool(
        val tool: Tools
    ): CanvasAction

    data class SelectLayer(
        val index: Int
    ): CanvasAction

    data class DragAndDropLayers(
        val fromIndex: Int,
        val toIndex: Int
    ): CanvasAction

    data class ChangeSliderPosition(
        val position: Float
    ): CanvasAction

    object ShowToolsBottomSheet: CanvasAction
    object HideToolsBottomSheet: CanvasAction
    object ConsumeEvent: CanvasAction


}