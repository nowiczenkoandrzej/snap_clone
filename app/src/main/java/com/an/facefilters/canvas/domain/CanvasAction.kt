package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import com.an.facefilters.canvas.data.filters.PhotoFilter
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
        val scale: Float,
        val rotation: Float,
        val offset: Offset
    ): EditingAction

    data class UpdateElementOrder(
        val fromIndex: Int,
        val toIndex: Int
    ): EditingAction

    data class ApplyFilter(val filter: PhotoFilter): EditingAction

    object RemoveBackground: EditingAction

    data class AddImage(val bitmap: Bitmap): EditingAction
    data class CropImage(val srcRect: Rect, val viewSize: IntSize): EditingAction
    data class SelectElement(val index: Int): EditingAction
    data class ChangeSliderPosition(val alpha: Float): EditingAction

    object DeleteElement: EditingAction

    data class LoadStickers(val category: StickerCategory): EditingAction
    data class AddSticker(val path: String): EditingAction
    data class CreateSticker(val bitmap: Bitmap): EditingAction

}

sealed interface ToolAction: CanvasAction {
    data class SelectTool(val tool: ToolType): ToolAction
    data class SetMode(val mode: Mode): ToolAction
    object Undo: ToolAction
    object Redo: ToolAction
    data class SelectColor(val color: Color): ToolAction
    data class AddText(val text: String): ToolAction
    data class SelectFontFamily(val fontFamily: FontFamily): ToolAction
    data class SelectAspectRatio(val aspectRatio: Float): ToolAction
    data class Save(val textMeasurer: TextMeasurer): ToolAction
}
