package com.an.feature_canvas.presentation


import android.net.Uri
import androidx.compose.ui.unit.IntSize
import com.an.core_editor.domain.model.Point
import com.an.feature_canvas.presentation.util.ToolType

sealed interface CanvasAction
sealed interface EditorAction: CanvasAction {
    data class TransformElement(
        val scaleDelta: Float,
        val rotationDelta: Float,
        val translation: Point
    ): EditorAction
    object TransformStart: EditorAction
    object TransformEnd: EditorAction

    data class SelectElement(val index: Int): EditorAction
    data class DeleteElement(val index: Int): EditorAction
    data class ReorderElements(
        val fromIndex: Int,
        val toIndex: Int
    ): EditorAction

    data class AddImage(
        val uri: Uri,
        val screenPadding: Float,
        val screenWidth: Float,
        val screenHeight: Float,
    ): EditorAction
    data class AddImageFromSavedProject(
        val path: String,
        val screenPadding: Float,
        val screenWidth: Float,
        val screenHeight: Float,
    ): EditorAction
    object Undo: EditorAction
    object NavigateToEditingScreen: EditorAction
}

sealed interface UiAction: CanvasAction {
    data class SelectTool(val tool: ToolType): UiAction
    object ToggleToolSelector: UiAction
    object ShowElementsPanel: UiAction
    data class SelectAspectRatio(val aspectRatio: Float): UiAction
    data class SetSize(val size: IntSize): UiAction
}
