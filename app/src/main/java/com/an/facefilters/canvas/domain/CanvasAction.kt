package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
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
}

sealed interface EditingAction: CanvasAction {

    data class TransformElement(
        val element: Element,
        val scale: Float,
        val rotation: Float,
        val offset: Offset
    ): EditingAction

    data class ApplyFilter(
        val filter: PhotoFilter,
        val element: Img,
    ): EditingAction

    data class CropImage(
        val srcRect: Rect,
        val viewSize: IntSize,
        val element: Img,
    ): EditingAction

    data class ChangeElementAlpha(
        val element: Element,
        val alpha: Float
    ): EditingAction

    data class RemoveBackground(
        val element: Img,
    ): EditingAction

}

sealed interface ElementAction: CanvasAction {

    data class UpdateElementOrder(
        val fromIndex: Int,
        val toIndex: Int
    ): ElementAction

    data class AddImage(val bitmap: Bitmap): ElementAction
    data class SelectElement(val index: Int): ElementAction
    data class UpdateElement(val element: Element): ElementAction

    object DeleteElement: ElementAction

}

sealed interface StickerAction: CanvasAction{
    data class LoadStickers(val category: StickerCategory): StickerAction
    data class AddSticker(val path: String): StickerAction
    data class CreateSticker(val bitmap: Bitmap): StickerAction
}


sealed interface ToolAction: CanvasAction {
    data class SelectTool(val tool: ToolType): ToolAction
    data class SetMode(val mode: Mode): ToolAction
    data class SelectColor(val color: Color): ToolAction
    data class AddText(val text: String): ToolAction
    data class SelectFontFamily(val fontFamily: FontFamily): ToolAction
    data class SelectAspectRatio(val aspectRatio: Float): ToolAction
    data class Save(val textMeasurer: TextMeasurer): ToolAction
}
