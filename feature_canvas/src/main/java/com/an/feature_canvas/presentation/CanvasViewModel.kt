package com.an.feature_canvas.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result
import com.an.core_editor.domain.model.handle
import com.an.core_editor.presentation.EditorUiState
import com.an.core_editor.presentation.toEditorUiState
import com.an.core_editor.domain.model.Point
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.presentation.UiTextModel
import com.an.feature_canvas.domain.use_cases.CanvasUseCases
import com.an.feature_canvas.presentation.util.PanelMode
import com.an.feature_canvas.presentation.util.ToolType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CanvasViewModel(
    private val editorRepository: EditorRepository,
    private val bitmapCache: BitmapCache,
    private val useCases: CanvasUseCases
): ViewModel() {

    val editorState = editorRepository
        .state
        .map { state -> state.toEditorUiState(bitmapCache) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EditorUiState())

    private val _uiState = MutableStateFlow(CanvasScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<CanvasEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    fun onAction(action: CanvasAction) {
        when(action){
            is EditorAction -> handleEditorAction(action)
            is UiAction -> handleUiAction(action)
        }
    }

    private fun handleEditorAction(action: EditorAction) {

        viewModelScope.launch {
            when(action) {
                is EditorAction.DeleteElement -> useCases.deleteElement(action.index).defaultHandle()
                is EditorAction.SelectElement -> {
                    useCases.selectElement(action.index).defaultHandle()

                }
                is EditorAction.TransformElement -> useCases.transformElement(
                    scaleDelta = action.scaleDelta,
                    rotationDelta = action.rotationDelta,
                    translation = action.translation,
                    saveUndo = false
                ).handle(
                    onFailure = {},
                    onSuccess = {
                        Log.d("TAG", "undo: transform")

                    }
                )
                EditorAction.TransformStart -> {
                    Log.d("TAG", "undo: transformstart")

                    useCases.transformElement(
                        scaleDelta = 1f,
                        rotationDelta = 0f,
                        translation = Point.ZERO,
                        saveUndo = true
                    )
                    _uiState.update { it.copy(
                        showToolsSelector = false
                    ) }
                }
                EditorAction.TransformEnd -> {}
                EditorAction.Undo -> useCases.undo().defaultHandle()
                is EditorAction.AddImage -> useCases.addImage(
                    uri = action.uri,
                    padding = action.screenPadding,
                    screenWidth = action.screenWidth,
                    screenHeight = action.screenHeight
                ).handle(
                    onSuccess = {
                        _uiState.update { it.copy(
                            panelMode = PanelMode.ELEMENTS
                        ) }
                    },
                    onFailure = { message ->
                        _events.send(CanvasEvent.ShowSnackbar(message))
                    }
                )
                is EditorAction.ReorderElements -> useCases.reorderElements(
                    from = action.fromIndex,
                    to = action.toIndex
                ).defaultHandle()

                EditorAction.NavigateToEditingScreen -> {
                    editorState.value.selectedElementIndex?.let {

                        when(editorState.value.elements[it]) {
                            is UiImageModel -> {
                                sendEvent(CanvasEvent.NavigateToEditImageScreen)
                            }
                            is UiTextModel -> {
                                sendEvent(CanvasEvent.NavigateToEditTextScreen)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun handleUiAction(action: UiAction) {
        when(action){
            UiAction.ToggleToolSelector -> _uiState.update { it.copy(
                showToolsSelector = !_uiState.value.showToolsSelector
            ) }

            is UiAction.SelectTool -> handleTool(action.tool)
            is UiAction.SelectAspectRatio -> _uiState.update { it.copy(
                aspectRatio = action.aspectRatio
            ) }

            UiAction.ShowElementsPanel -> _uiState.update { it.copy(
                panelMode = PanelMode.ELEMENTS
            ) }
        }
    }

    private fun handleTool(tool: ToolType) {
        when(tool) {
            ToolType.PICK_IMAGE_FROM_GALLERY -> sendEvent(CanvasEvent.PickImageFromGallery)
            ToolType.SAVE -> showSnackBar("Not Implemented Yet")
            ToolType.ADD_TEXT -> {
                sendEvent(CanvasEvent.NavigateToAddTextScreen)
                _uiState.update { it.copy(
                    panelMode = PanelMode.ELEMENTS
                ) }
            }
            ToolType.ASPECT_RATIO -> _uiState.update { it.copy(
                panelMode = PanelMode.ASPECT_RATIO
            ) }
            ToolType.STICKERS -> {
                sendEvent(CanvasEvent.NavigateToStickersScreen)
                _uiState.update { it.copy(
                    panelMode = PanelMode.ELEMENTS
                ) }
            }
        }
        _uiState.update { it.copy(
            showToolsSelector = false
        ) }
    }

    private fun sendEvent(event: CanvasEvent) = viewModelScope.launch {
        _events.send(event)
    }

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _events.send(CanvasEvent.ShowSnackbar(message))
    }

    private fun Result<Unit>.defaultHandle() {
        this.handle(
            onSuccess = {},
            onFailure = ::showSnackBar
        )
    }





}