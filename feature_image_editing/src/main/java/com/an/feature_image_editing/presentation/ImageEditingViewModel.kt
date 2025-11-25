package com.an.feature_image_editing.presentation

import android.graphics.Bitmap
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.ImageRenderer
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.handle
import com.an.core_editor.presentation.mappers.toDomain
import com.an.core_editor.presentation.mappers.toOffset
import com.an.core_editor.presentation.mappers.toOffsetList
import com.an.core_editor.presentation.mappers.toPoint
import com.an.core_editor.presentation.model.UiImageModel
import com.an.feature_image_editing.domain.use_cases.EditingUseCases
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
    private val renderer: ImageRenderer,
    private val useCases: EditingUseCases,
    private val bitmapCache: BitmapCache
): ViewModel() {

    private var currentVersion = mutableLongStateOf(1)
    private var currentBitmap = mutableStateOf<Bitmap?>(null)

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


    private val _drawingState = MutableStateFlow(DrawingState())
    val drawingState = _drawingState.asStateFlow()

    private val _rubberState = MutableStateFlow(RubberState())
    val rubberState = _rubberState.asStateFlow()

    private val _events = Channel<EditingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: ImageEditingAction) {
        when(action) {
            is DrawingAction -> handleDrawingAction(action)
            is EditingAction -> handleEditingAction(action)
            is RubberAction -> handleRubberAction(action)
        }

    }

    private fun handleRubberAction(action: RubberAction) {
        when(action) {
            RubberAction.AddNewPath -> {

                val editedBitmap = if(_rubberState.value.changesStack.isEmpty()) {
                    editedImageModel.value?.bitmap
                } else {
                    _rubberState.value.changesStack.last()
                }

                if(editedBitmap == null) {
                    viewModelScope.launch {
                        _events.send(EditingEvent.ShowSnackbar("Something went wrong..."))
                    }
                    return
                }

                useCases.erasePathFromBitmap(
                    bitmap = editedBitmap,
                    path = _rubberState.value.currentPath.path.toOffsetList(),
                    thickness = _rubberState.value.currentPath.thickness
                ).handle(
                    onSuccess = { newBitmap ->
                        _rubberState.update {
                            it.copy(
                                changesStack = it.changesStack.plus(newBitmap),
                                rubberPaths = it.rubberPaths + it.currentPath,
                                currentPath = it.currentPath.copy(
                                    path = emptyList()
                                )
                            )
                        }
                    },
                    onFailure = { message ->
                        viewModelScope.launch {
                            _events.send(EditingEvent.ShowSnackbar(message))
                        }
                    }
                )

            }
            RubberAction.Cancel -> {
                _rubberState.update {
                    it.copy(
                        currentPath = it.currentPath.copy(
                            path = emptyList()
                        ),
                        changesStack = emptyList()
                    )
                }
                viewModelScope.launch {
                    _events.send(EditingEvent.PopBackStack)
                }
            }
            RubberAction.SaveRubber -> viewModelScope.launch {

                if(_rubberState.value.changesStack.isNotEmpty()) {
                    useCases.applyRubber(
                        paths = rubberState.value.rubberPaths
                    ).handle(
                        onSuccess = {
                            _rubberState.update { it.copy(
                                changesStack = emptyList()
                            ) }
                            _events.send(EditingEvent.PopBackStack)
                        },
                        onFailure = { message ->
                            _events.send(EditingEvent.ShowSnackbar(message))
                        }
                    )
                } else {
                    _events.send(EditingEvent.PopBackStack)
                }
            }
            is RubberAction.SelectThickness -> _rubberState.update { it.copy(
                pathThickness = action.thickness
            ) }
            RubberAction.UndoPath -> {
                if(_rubberState.value.changesStack.isNotEmpty()) {
                    _rubberState.update {
                        it.copy(
                            changesStack = it.changesStack.dropLast(1)
                        )
                    }}
            }
            is RubberAction.UpdateCurrentPath -> {
                _rubberState.update {
                    it.copy(
                        currentPath = it.currentPath.copy(
                            path = it.currentPath.path + action.offset.toPoint(),
                            thickness = it.pathThickness * (1 / action.scale)
                        )
                    )
                }


            }
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
                        left = action.srcRect.left,
                        top = action.srcRect.top,
                        width = action.srcRect.width,
                        height = action.srcRect.height,
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
            DrawingAction.Cancel -> {
                _drawingState.update {
                    it.copy(
                        paths = emptyList(),
                        currentPath = it.currentPath.copy(
                            path = emptyList()
                        )
                    )
                }
                viewModelScope.launch {
                    _events.send(EditingEvent.PopBackStack)
                }
            }
            DrawingAction.SaveDrawings -> viewModelScope.launch {
                useCases.saveDrawings(
                    paths = _drawingState.value.paths
                ).handle(
                    onSuccess = {
                        _events.send(EditingEvent.PopBackStack)
                    },
                    onFailure = { message ->
                        _events.send(EditingEvent.ShowSnackbar(message))
                    }
                )
                _drawingState.update { it.copy(
                    paths = emptyList()
                ) }

            }
            is DrawingAction.SelectThickness -> _drawingState.update { it.copy(
                 pathThickness = action.thickness
            ) }
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
                    }}
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

            is DrawingAction.SelectColor -> _drawingState.update { it.copy(
                selectedColor = action.color
            ) }
        }
    }


}