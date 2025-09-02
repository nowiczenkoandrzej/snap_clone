package com.an.feature_image_editing.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.presentation.toOffset
import com.an.core_editor.presentation.toOffsetList
import com.an.core_editor.presentation.toPoint
import com.an.core_editor.presentation.toUiImageModel
import com.an.feature_image_editing.domain.use_cases.EditingUseCases
import com.an.feature_image_editing.presentation.util.drawPaths
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImageEditingViewModel(
    private val editorRepository: EditorRepository,
    private val bitmapCache: BitmapCache,
    private val useCases: EditingUseCases
): ViewModel() {

    val editedImageModel: StateFlow<UiImageModel?> =
        editorRepository.state
            .map { state ->
                state.selectedElementIndex
                    ?.let { index -> state.elements.getOrNull(index) }
                    ?.let { element ->
                        if (element is DomainImageModel) {
                            element.toUiImageModel(bitmapCache)
                        } else null
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )


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
        viewModelScope.launch {
            when(action) {
                is EditingAction.ApplyFilter -> useCases.applyFilter(
                    filter = action.filter,
                )
                is EditingAction.ChangeElementAlpha -> useCases.changeElementAlpha(
                    newAlpha = action.alpha
                )
                is EditingAction.CropImage -> {
                    useCases.cropImage(
                        srcRect = action.srcRect,
                        viewSize = action.viewSize
                    )
                    _events.send(EditingEvent.PopBackStack)
                }
                EditingAction.RemoveBackground -> useCases.removeBackground()
                EditingAction.DeleteImage -> {
                    useCases.deleteImage()
                    _events.send(EditingEvent.PopBackStack)
                }
                EditingAction.CancelCropping -> _events.send(EditingEvent.PopBackStack)
            }

        }
    }

    private fun handleDrawingAction(action: DrawingAction) {
        when(action) {
            DrawingAction.AddNewPath -> {
                _drawingState.update {
                    it.copy(
                        paths = it.paths + it.currentPath,
                        currentPath = it.currentPath.copy(
                            path = emptyList()
                        )
                    )
                }


            }
            DrawingAction.Cancel -> _drawingState.update { it.copy(
                paths = emptyList(),
                currentPath = it.currentPath.copy(
                    path = emptyList()
                )
            ) }
            DrawingAction.SaveDrawings -> viewModelScope.launch {
                useCases.saveDrawings(
                    paths = _drawingState.value.paths
                )
            }
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
            is DrawingAction.UpdateCurrentPath -> {
                _drawingState.update {
                    it.copy(
                        currentPath = it.currentPath.copy(
                            path = it.currentPath.path + action.offset.toPoint(),
                            color = it.selectedColor,
                            thickness = it.pathThickness
                        )
                    )
                }


            }

            is DrawingAction.SelectColor -> _drawingState.update { it.copy(
                selectedColor = action.color
            ) }
        }
    }


}