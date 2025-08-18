package com.an.feature_image_editing.presentation

import androidx.lifecycle.ViewModel
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.presentation.toOffset
import com.an.feature_image_editing.presentation.components.EditingUiState
import com.an.feature_image_editing.presentation.util.PathData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class ImageEditingViewModel(
    private val editorRepository: EditorRepository,
    val bitmapCache: BitmapCache
): ViewModel() {

    val editorState = editorRepository.state

    private val _uiState = MutableStateFlow(EditingUiState())
    val uiState = _uiState.asStateFlow()

    private val _drawingState = MutableStateFlow(DrawingState())
    val drawingState = _drawingState.asStateFlow()

    private val _events = Channel<EditingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: ImageEditingAction) {
        when(action) {
            is DrawingAction -> handleDrawingAction(action)
            is EditingAction -> handleEditingAction(action)
        }

    }

    private fun handleEditingAction(action: EditingAction) {
        when(action) {
            is EditingAction.ApplyFilter -> TODO()
            is EditingAction.ChangeElementAlpha -> TODO()
            is EditingAction.CropImage -> TODO()
            EditingAction.RemoveBackground -> TODO()
            EditingAction.DeleteImage -> TODO()
            EditingAction.CancelCropping -> TODO()
        }
    }

    private fun handleDrawingAction(action: DrawingAction) {
        when(action) {
            DrawingAction.AddNewPath -> _drawingState.update { it.copy(
                paths = it.paths + it.currentPath,
                currentPath = it.currentPath.copy(
                    path = emptyList()
                )
            ) }
            DrawingAction.Cancel -> _drawingState.update { it.copy(
                paths = emptyList(),
                currentPath = it.currentPath.copy(
                    path = emptyList()
                )
            ) }
            DrawingAction.SaveDrawings -> TODO()
            is DrawingAction.SelectThickness -> _drawingState.update { it.copy(
                 pathThickness = action.thickness
            ) }
            DrawingAction.UndoPath -> _drawingState.update { it.copy(
                paths = _drawingState
                    .value
                    .paths
                    .toMutableList()
                    .apply {
                        removeAt(this.lastIndex)
                    }
                    .toList()
            ) }
            is DrawingAction.UpdateCurrentPath -> _drawingState.update { it.copy(
                currentPath = it.currentPath.copy(
                    path = it.currentPath.path + action.point.toOffset()
                )
            ) }

            is DrawingAction.SelectColor -> _drawingState.update { it.copy(
                selectedColor = action.color
            ) }
        }
    }


}