package com.an.feature_drawing.presentation

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.presentation.mappers.toDomain
import com.an.core_editor.presentation.mappers.toOffset
import com.an.core_editor.presentation.mappers.toPoint
import com.an.core_editor.presentation.model.UiImageModel
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

class DrawingViewModel(
    private val editorRepository: EditorRepository
): ViewModel() {

    private var currentBitmap = mutableStateOf<Bitmap?>(null)

    private val _drawingState = MutableStateFlow(DrawingState())
    val drawingState = _drawingState.asStateFlow()

    private val _events = Channel<DrawingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    val editedImageModel: StateFlow<UiImageModel?> =
        editorRepository.state
            .map { state ->

                state.selectedElementIndex
                    ?.let { index -> state.elements.getOrNull(index) }
                    ?.let { element ->
                        if (element is DomainImageModel) {

                            currentBitmap.value = if(element.version == currentVersion.longValue) {
                                currentBitmap.value
                            } else {
                                currentVersion.longValue = element.version

                                val rendered = renderer.render(element)
                                if(rendered != null)
                                    bitmapCache.addEdited(element.id, rendered)
                                rendered
                            }

                            UiImageModel(
                                rotationAngle = element.rotationAngle,
                                scale = element.scale,
                                alpha = element.alpha,
                                position = element.position.toOffset(),
                                bitmap = currentBitmap.value,
                                currentFilter = element.currentFilter,
                                version = element.version
                            )
                        } else {
                            null
                        }
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    fun onAction(action: DrawingAction) {
        when(action) {
            DrawingAction.AddNewPath -> {
                _drawingState.update { it.copy(
                    paths = it.paths + it.currentPath,
                    currentPath = PathData.DEFAULT
                ) }
            }
            DrawingAction.Cancel -> {
                _drawingState.update { it.copy(
                    paths = emptyList(),
                    currentPath = it.currentPath.copy(
                        path = emptyList()
                    )
                ) }
                viewModelScope.launch {
                    _events.send(DrawingEvent.Cancel)
                }
            }
            DrawingAction.SaveDrawings ->  {

            }
            is DrawingAction.SelectColor -> {
                _drawingState.update { it.copy(
                    selectedColor = action.color
                ) }
            }
            is DrawingAction.SelectThickness -> {
                _drawingState.update { it.copy(
                    pathThickness = action.thickness
                ) }
            }
            is DrawingAction.SetMode -> {
                _drawingState.update { it.copy(
                    mode = action.mode
                ) }
            }
            DrawingAction.UndoPath -> {
                if(_drawingState.value.paths.isNotEmpty()) {
                    _drawingState.update {
                        it.copy(
                            paths = _drawingState
                                .value
                                .paths
                                .toMutableList()
                                .apply {
                                    removeAt(this.lastIndex)
                                }
                                .toList()
                        )
                    }
                }
            }
            is DrawingAction.UpdateCurrentPath -> {
                _drawingState.update {
                    it.copy(
                        currentPath = it.currentPath.copy(
                            path = it.currentPath.path + action.offset.toPoint(),
                            color = it.selectedColor.toDomain(),
                            thickness = it.pathThickness / action.scale
                        )
                    )
                }

            }
        }
    }

}