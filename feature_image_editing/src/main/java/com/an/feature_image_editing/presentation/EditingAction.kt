package com.an.feature_image_editing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.an.core_editor.domain.model.Point
import com.an.feature_image_editing.presentation.util.PhotoFilter

sealed interface ImageEditingAction

sealed interface EditingAction: ImageEditingAction {
    data class ApplyFilter(
        val filter: PhotoFilter,
    ): EditingAction
    data class CropImage(
        val srcRect: Rect,
        val viewSize: IntSize,
    ): EditingAction
    object CancelCropping: EditingAction
    data class ChangeElementAlpha(
        val alpha: Float
    ): EditingAction

    object RemoveBackground: EditingAction
    object DeleteImage: EditingAction

}

sealed interface DrawingAction: ImageEditingAction {
    data class SelectThickness(val thickness: Float): DrawingAction
    data class UpdateCurrentPath(val offset: Offset, val scale: Float): DrawingAction
    data class SelectColor(val color: Color): DrawingAction
    object AddNewPath: DrawingAction
    object SaveDrawings: DrawingAction
    object Cancel: DrawingAction
    object UndoPath: DrawingAction
}

sealed interface RubberAction: ImageEditingAction {
    data class SelectThickness(val thickness: Float): RubberAction
    data class UpdateCurrentPath(val offset: Offset, val scale: Float): RubberAction
    object AddNewPath: RubberAction
    object SaveRubber: RubberAction
    object Cancel: RubberAction
    object UndoPath: RubberAction
}

