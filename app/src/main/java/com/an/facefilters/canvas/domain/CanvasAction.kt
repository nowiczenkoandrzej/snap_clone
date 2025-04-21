package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset

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


}