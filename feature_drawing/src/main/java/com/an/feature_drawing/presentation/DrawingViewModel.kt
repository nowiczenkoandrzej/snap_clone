package com.an.feature_drawing.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.presentation.mappers.toDomain
import com.an.core_editor.presentation.mappers.toOffset
import com.an.core_editor.presentation.mappers.toPoint
import com.an.core_editor.presentation.model.UiImageModel
import com.an.core_project.domain.ProjectEditor
import com.an.feature_drawing.domain.use_cases.DrawingUseCases
import com.an.feature_drawing.presentation.util.DrawingMode
import com.an.feature_drawing.presentation.util.DrawingModeArg
import com.an.feature_drawing.presentation.util.toDrawingMode
import com.an.feature_image_caching.BitmapCache
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DrawingViewModel(
    private val projectEditor: ProjectEditor,
    private val bitmapCache: BitmapCache,
    private val savedStateHandle: SavedStateHandle,
    private val useCases: DrawingUseCases
): ViewModel() {

    val editedImage = projectEditor
        .selectedImage
        .map { domainImage ->
            if(domainImage == null) {
                null
            } else {
                UiImageModel(
                    rotationAngle = domainImage.rotationAngle,
                    scale = domainImage.scale,
                    alpha = domainImage.alpha,
                    position = domainImage.position.toOffset(),
                    bitmap = bitmapCache.getEditedBitmap(domainImage.imagePath),
                    currentFilter = domainImage.currentFilter,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val modeArg: String = checkNotNull(savedStateHandle["mode"])

    val drawingMode: DrawingMode = DrawingModeArg.valueOf(modeArg).toDrawingMode()
    private val _drawingState = MutableStateFlow(DrawingState(mode = drawingMode))
    val drawingState = _drawingState.asStateFlow()

    private val _events = Channel<DrawingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    

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
                useCases.saveDrawings
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