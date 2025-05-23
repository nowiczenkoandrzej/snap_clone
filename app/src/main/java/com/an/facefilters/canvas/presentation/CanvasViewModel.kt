package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.CanvasState
import com.an.facefilters.canvas.domain.DrawingAction
import com.an.facefilters.canvas.domain.LayerAction
import com.an.facefilters.canvas.domain.ToolAction
import com.an.facefilters.canvas.domain.UiAction
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Layer
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.PathData
import com.an.facefilters.canvas.domain.model.TextModel
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.domain.model.Undo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Stack

class CanvasViewModel(
    private val subjectDetector: SubjectDetector
): ViewModel() {
    private val _screenState = MutableStateFlow(CanvasState())
    val screenState = _screenState.asStateFlow()

    private val _events = Channel<CanvasEvent?>()
    val events = _events.receiveAsFlow()

    private val undos = Stack<Undo>()
    private val redos = Stack<Undo>()

    fun onAction(action: CanvasAction) {

        when(action) {
            is ToolAction -> handleToolAction(action)
            is UiAction -> handleUiAction(action)
            is DrawingAction -> handleDrawingAction(action)
            is LayerAction -> handleLayerAction(action)
        }
    }

    private fun handleToolAction(action: ToolAction) {
        when(action) {
            is ToolAction.SelectColor -> _screenState.update { it.copy(
                    selectedColor = action.color,
                    showColorPicker = false
                ) }
            ToolAction.SelectLayersMode -> _screenState.update { it.copy(selectedMode = Mode.LAYERS) }
            is ToolAction.SetMode -> _screenState.update { it.copy(selectedMode = action.mode) }
            is ToolAction.AddText -> addText(action.text)
            is ToolAction.SelectTool -> selectTool(action.tool)
            ToolAction.Undo -> undo()
            ToolAction.Redo -> redo()
        }
    }

    private fun handleUiAction(action: UiAction) {
        when(action) {
            UiAction.ConsumeEvent -> viewModelScope.launch { _events.send(null) }
            UiAction.HideColorPicker -> _screenState.update { it.copy(showColorPicker = false) }
            UiAction.HideTextInput -> _screenState.update { it.copy(showTextInput = false) }
            UiAction.HideToolsSelector -> _screenState.update { it.copy(showToolsSelector = false) }
            UiAction.ShowColorPicker -> _screenState.update { it.copy(showColorPicker = true) }
            UiAction.ShowTextInput -> _screenState.update { it.copy(showTextInput = true) }
            UiAction.ShowToolsSelector -> _screenState.update { it.copy(showToolsSelector = true) }
        }
    }

    private fun handleDrawingAction(action: DrawingAction) {
        when(action) {
            is DrawingAction.DrawPath -> drawPath(action.offset)
            DrawingAction.EndDrawingPath -> addPath()
            DrawingAction.StartDrawingPath -> saveUndo()
            is DrawingAction.SelectThickness -> {
                _screenState.update { it.copy(
                    pathThickness = action.thickness
                ) }
            }
        }
    }

    private fun detectSubject() {
        val index = screenState.value.selectedLayerIndex
        if(index == null) {
            _screenState.update { it.copy(
                showToolsSelector = false
            ) }
            viewModelScope.launch {
                _events.send(CanvasEvent.ShowToast("Pick Image"))
            }
            return
        }
        val currentBitmap = screenState.value.layers[index]
        if(currentBitmap !is Img) {
            _screenState.update { it.copy(
                showToolsSelector = false
            ) }
            viewModelScope.launch {
                _events.send(CanvasEvent.ShowToast("Pick Image"))
            }
            return
        }
        subjectDetector.detectSubject(
            bitmap = currentBitmap.bitmap,
            onSubjectDetected = { bitmap ->
                val updatedLayers = _screenState
                    .value
                    .layers
                    .toMutableList()
                    .apply {
                        set(index, Img(bitmap = bitmap))
                    }
                    .toList()
                _screenState.update { it.copy(
                    layers = updatedLayers,
                    showToolsSelector = false
                ) }
            },
            onError = { error ->
                viewModelScope.launch {
                    _events.send(CanvasEvent.ShowToast(error))
                }
            }
        )
    }

    private fun handleLayerAction(action: LayerAction) {
        when(action) {
            is LayerAction.AddImage -> addImage(action.bitmap)
            is LayerAction.DragAndDropLayers -> dragAndDrop(action.fromIndex, action.toIndex)
            is LayerAction.ChangeSliderPosition -> _screenState.update { it.copy(alphaSliderPosition = action.position) }
            is LayerAction.SelectLayer -> _screenState.update { it.copy(selectedLayerIndex = action.index) }
            is LayerAction.TransformLayer -> transformLayer(action)
            LayerAction.TransformStart -> saveUndo()

        }
    }

    private fun addText(text: String) {
        saveUndo()
        _screenState.update { it.copy(
            layers = _screenState.value.layers + TextModel(
                text = text,
                textStyle = TextStyle(
                    fontSize = 60.sp
                ),
                p1 = Offset(0f, 0f)
            ),
            showTextInput = false
        ) }
    }

    private fun undo() {
        if(undos.isNotEmpty()) {
            val previousState = undos.pop()
            redos.push(Undo(
                layers = _screenState.value.layers,
                paths = _screenState.value.paths
            ))
            _screenState.update { it.copy(
                layers = previousState.layers,
                paths = previousState.paths
            ) }
        }
    }
    private fun redo() {
        if(redos.isNotEmpty()) {
            val nextState = redos.pop()
            saveUndo()
            _screenState.update { it.copy(
                layers = nextState.layers,
                paths = nextState.paths
            ) }
        }
    }
    private fun addImage(bitmap: Bitmap) {
        saveUndo()
        _screenState.update { it.copy(
            layers = screenState.value.layers + Img(bitmap = bitmap),
            selectedLayerIndex = screenState.value.layers.size,
            selectedMode = Mode.LAYERS
        ) }
    }
    private fun addPath() {
        val newPath = _screenState.value.drawnPath ?: return
        _screenState.update { it.copy(
            paths = _screenState.value.paths + newPath,
            drawnPath = null
        ) }
    }
    private fun saveUndo() {
        undos.push(Undo(
            layers = _screenState.value.layers,
            paths = _screenState.value.paths
        ))
    }
    private fun transformLayer(action: LayerAction.TransformLayer) {
        if(screenState.value.selectedLayerIndex == null) return
        if(screenState.value.layers.size <= screenState.value.selectedLayerIndex!!) {
            _screenState.update { it.copy(
                selectedLayerIndex = null
            ) }
            return
        }
        redos.clear()
        val updatedLayer = screenState
            .value
            .layers[screenState.value.selectedLayerIndex!!]
            .transform(
                scale = action.scale,
                rotation = action.rotation,
                offset = action.offset
            )
        updateLayer(updatedLayer)
    }
    private fun dragAndDrop(from: Int, to: Int) {
        val updatedLayer = screenState
            .value
            .layers
            .toMutableList()
            .apply {
                add(to, removeAt(from))
            }
        _screenState.update { it.copy(
            layers = updatedLayer,
            selectedLayerIndex = to
        )}
    }
    private fun updateLayer(newLayer: Layer) {
        val newList = screenState.value.layers.toMutableList().apply {
            this[screenState.value.selectedLayerIndex!!] = newLayer
        }.toList()

        _screenState.update { it.copy(
            layers = newList,
            showToolsSelector = false
        ) }
    }
    private fun drawPath(offset: Offset) {
        redos.clear()
        val currentPath = _screenState.value.drawnPath?.path
        if(currentPath == null) {
            _screenState.update { it.copy(
                drawnPath = PathData(
                    color = _screenState.value.selectedColor,
                    path = emptyList<Offset>() + offset,
                    thickness = _screenState.value.pathThickness
                ),
                showToolsSelector = false
            ) }
        } else {
            _screenState.update { it.copy(
                drawnPath = PathData(
                    color = _screenState.value.selectedColor,
                    path = currentPath + offset,
                    thickness = _screenState.value.pathThickness
                ),
                showToolsSelector = false
            ) }
        }
    }
    private fun selectTool(type: ToolType) {
        when(type) {
            ToolType.AddPhoto -> {
                viewModelScope.launch {
                    _events.send(CanvasEvent.PickImage)
                }
                _screenState.update { it.copy(
                    showToolsSelector = false
                ) }
            }
            ToolType.Pencil -> {
                _screenState.update { it.copy(
                    selectedMode = Mode.PENCIL,
                    showToolsSelector = false
                ) }
            }
            ToolType.Text -> {
                _screenState.update { it.copy(
                    selectedMode = Mode.TEXT,
                    showToolsSelector = false,
                    showTextInput = true
                ) }
            }
            ToolType.RemoveBg -> {
                detectSubject()
            }
            ToolType.CropImage -> {
                viewModelScope.launch {
                    _screenState.value.selectedLayerIndex?.let {
                        if(_screenState.value.layers[_screenState.value.selectedLayerIndex!!] is Img) {
                            _events.send(CanvasEvent.CropImage)
                        } else {
                            _events.send(CanvasEvent.ShowToast("Pick Image"))
                        }
                    }?: _events.send(CanvasEvent.ShowToast("Pick Image"))

                }
            }
        }
    }
}