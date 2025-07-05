package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import android.provider.ContactsContract.Contacts.Photo
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.ToolType
import kotlin.js.ExperimentalJsFileName

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

sealed interface ElementAction: CanvasAction {
    object TransformStart: ElementAction

    data class TransformElement(
        val scale: Float,
        val rotation: Float,
        val offset: Offset
    ): ElementAction

    data class DragAndDropElement(
        val fromIndex: Int,
        val toIndex: Int
    ): ElementAction

    data class ApplyFilter(val filter: PhotoFilter): ElementAction

    data class AddImage(val bitmap: Bitmap): ElementAction
    data class CropImage(val srcRect: Rect, val viewSize: IntSize): ElementAction
    data class SelectElement(val index: Int): ElementAction
    data class ChangeSliderPosition(val alpha: Float): ElementAction

    object DeleteElement: ElementAction

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
}

sealed interface StickerAction: CanvasAction {
    object LoadCategories: StickerAction
    data class LoadStickers(val category: StickerCategory): StickerAction
    data class AddSticker(val path: String): StickerAction
    data class CreateSticker(val bitmap: Bitmap): StickerAction
}