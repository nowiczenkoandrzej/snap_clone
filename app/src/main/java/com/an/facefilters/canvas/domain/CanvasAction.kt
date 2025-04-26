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

    data class SelectLayer(
        val offset: Offset
    ): CanvasAction

    data class InsertInitialBitmap(
        val bitmap: Bitmap
    ): CanvasAction

    data class AddImage(
        val bitmap: Bitmap
    ): CanvasAction

    data class SelectTool(
        val tool: Tools
    ): CanvasAction

    data class ChangeLayer(
        val index: Int
    ): CanvasAction

    object ShowToolsBottomSheet: CanvasAction
    object HideToolsBottomSheet: CanvasAction
    object EndGesture: CanvasAction
    object ConsumeEvent: CanvasAction


}