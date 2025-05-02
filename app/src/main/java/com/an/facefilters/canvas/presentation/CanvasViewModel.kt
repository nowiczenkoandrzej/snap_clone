package com.an.facefilters.canvas.presentation

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.CanvasState
import com.an.facefilters.canvas.domain.PathData
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Layer
import com.an.facefilters.canvas.domain.model.Mode
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

): ViewModel() {

    private val _screenState = MutableStateFlow(CanvasState())
    val screenState = _screenState.asStateFlow()

    private val _events = Channel<CanvasEvent?>()
    val events = _events.receiveAsFlow()

    private val undos = Stack<Undo>()
    private val redos = Stack<Undo>()



    fun onAction(action: CanvasAction) {

        when(action) {
            is CanvasAction.TransformLayer -> transformLayer(action)
            is CanvasAction.DragAndDropLayers -> dragAndDrop(action.fromIndex, action.toIndex)
            is CanvasAction.SelectTool -> selectTool(action.tool)
            is CanvasAction.DrawPath -> drawPath(action.offset)

            CanvasAction.EndDrawingPath -> {
                val newPath = _screenState.value.drawnPath ?: return
                _screenState.update { it.copy(
                    paths = _screenState.value.paths + newPath,
                    drawnPath = null
                ) }
            }

            is CanvasAction.AddImage -> {
                saveUndo()
                _screenState.update { it.copy(
                    layers = screenState.value.layers + Img(bitmap = action.bitmap),
                    selectedLayerIndex = screenState.value.layers.size,
                    selectedMode = Mode.LAYERS
                ) }
            }


            CanvasAction.HideToolsSelector -> {
                _screenState.update { it.copy(
                    showToolsSelector = false
                ) }
            }
            CanvasAction.ShowToolsSelector -> {
                _screenState.update { it.copy(
                    showToolsSelector = true
                ) }
            }

            CanvasAction.ConsumeEvent -> {
                viewModelScope.launch {
                    _events.send(null)
                }
            }

            is CanvasAction.SelectLayer -> {
                _screenState.update { it.copy(
                    selectedLayerIndex = action.index
                ) }
            }

            is CanvasAction.ChangeSliderPosition -> {
                _screenState.update { it.copy(
                    alphaSliderPosition = action.position
                ) }
            }

            is CanvasAction.SetMode -> {
                _screenState.update { it.copy(
                    selectedMode = action.mode
                ) }
            }

            CanvasAction.SelectLayersMode -> {
                _screenState.update { it.copy(
                    selectedMode = Mode.LAYERS
                ) }
            }

            CanvasAction.TransformStart -> saveUndo()

            CanvasAction.Redo -> {
                if(redos.isNotEmpty()) {
                    val nextState = redos.pop()
                    saveUndo()
                    _screenState.update { it.copy(
                        layers = nextState.layers,
                        paths = nextState.paths
                    ) }
                }
            }
            CanvasAction.Undo -> {
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

            CanvasAction.StartDrawingPath -> saveUndo()

        }
    }

    private fun saveUndo() {
        undos.push(Undo(
            layers = _screenState.value.layers,
            paths = _screenState.value.paths
        ))
    }


    private fun transformLayer(action: CanvasAction.TransformLayer) {
        if(screenState.value.selectedLayerIndex == null) return

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
            layers = newList
        ) }
    }

    private fun drawPath(offset: Offset) {
        redos.clear()
        val currentPath = _screenState.value.drawnPath?.path

        if(currentPath == null) {
            _screenState.update { it.copy(
                drawnPath = PathData(
                    color = _screenState.value.selectedColor,
                    path = emptyList<Offset>() + offset
                )
            ) }
        } else {
            _screenState.update { it.copy(
                drawnPath = PathData(
                    color = _screenState.value.selectedColor,
                    path = currentPath + offset
                )
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
        }
    }


}