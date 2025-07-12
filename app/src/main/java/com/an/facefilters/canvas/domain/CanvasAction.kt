package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.domain.model.CanvasMode
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.PanelMode
import com.an.facefilters.canvas.domain.model.ToolType

sealed interface CanvasAction

sealed interface EditingAction: CanvasAction {
    data class TransformElement(
        val scale: Float,
        val rotation: Float,
        val offset: Offset
    ): EditingAction
    data class ApplyFilter(
        val filter: PhotoFilter,
    ): EditingAction

    data class CropImage(
        val srcRect: Rect,
        val viewSize: IntSize,
    ): EditingAction

    data class ChangeElementAlpha(
        val alpha: Float
    ): EditingAction

    object RemoveBackground: EditingAction

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

    data class AddText(val text: String): ElementAction
    data class SelectFontFamily(val fontFamily: FontFamily): ElementAction

}


sealed interface StickerAction: CanvasAction{
    data class LoadStickersByCategory(val category: StickerCategory): StickerAction
    data class AddSticker(val path: String): StickerAction
    data class CreateSticker(val bitmap: Bitmap): StickerAction
}

sealed interface UiAction: CanvasAction{
    object ShowToolsSelector: UiAction
    object HideToolsSelector: UiAction
    object ShowColorPicker: UiAction
    object HideColorPicker: UiAction
    object ShowTextInput: UiAction
    object HideTextInput: UiAction
    data class SetPanelMode(val mode: PanelMode): UiAction
    data class SetCanvasMode(val mode: CanvasMode): UiAction
    data class SelectColor(val color: Color): UiAction
    data class SelectAspectRatio(val aspectRatio: Float): UiAction

    data class SelectTool(val toolType: ToolType): UiAction

    data class Save(val textMeasurer: TextMeasurer): UiAction

}

